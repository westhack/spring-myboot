// 下划线转换驼峰
function camelCase(name) {
  return name.replace(/\_(\w)/g, function(all, letter) {
    return letter.toUpperCase()
  })
}

function jsonToPojoConverter() {
  const instance = {}

  function mergeArrayObjects(objArray) {
    const result = {}

    for (let i = 0; i < objArray.length; i++) {
      for (const field in objArray[i]) {
        if (!result.hasOwnProperty(field)) {
          result[field] = objArray[i][field]
        }
      }
    }

    return result
  }

  function capitalize (str) {
    return str[0].toUpperCase() + str.slice(1)
  }

  function getJavaType (type) {
    switch (type) {
      case 'array':
        return 'List'
      case 'object':
      case 'string':
      case 'date': // should be String?
      case 'integer':
      case 'double':
      case 'boolean':
        return capitalize(type)
      default:
        return type
    }
  }

  function getType (val) {
    const typeInfo = {
      'type': typeof val
    }
    if (val == null) {
      typeInfo.type = 'string'
    }

    switch (typeInfo.type) {
      case 'object':
        // if the object is an array, get type of array content
        // otherwise, get the definition of the object value itself
        if (Array.isArray(val)) {
          typeInfo.type = 'array'

          if (typeof val[0] === 'object') {
            typeInfo.definition = getType(mergeArrayObjects(val))
          } else {
            typeInfo.definition = getType(val[0])
          }
        } else {
          typeInfo.definition = getObjectDefinition(val)
        }

        break
      case 'string':
        // eslint-disable-next-line no-useless-escape
        if (/(\d{2}|\d{4})[\-\\\/]\d{1,2}[\-\\\/]\d{1,2}/.test(val)) {
          typeInfo.type = 'date'
        }

        break
      case 'number':
        if (Number.isInteger(val)) {
          typeInfo.type = 'integer'
        } else {
          typeInfo.type = 'double'
        }

        break
    }

    return typeInfo
  }

  function getObjectDefinition (obj) {
    const objectDefinition = {}

    // create a definition object that contains a map
    // of field names to field types, recursing on object
    // field types
    for (const field in obj) {
      objectDefinition[field] = getType(obj[field])
    }

    return objectDefinition
  }

  function getJavaClassDefinition (className, fields, isGetSet, isApi) {
    let i
    let result = ''

    result += 'public class ' + className + ' {\n\n'

    // output list of private fields
    for (i = 0; i < fields.length; i++) {
      if (isApi) {
        result += '    @ApiModelProperty(value = "' + fields[i].fieldJson + '")\n'
      }
      result += '    @JsonProperty("' + fields[i].fieldJson + '")\n'
      result += '    private ' + fields[i].typeDeclaration + ' ' + fields[i].fieldName + ';\n\n'
    }

    result += '\n\n'

    // output default constructor
    result += '    public ' + className + '() {}\n\n'

    // output constructor parameters
    result += '    public ' + className + '(\n'
    for (i = 0; i < fields.length; i++) {
      result += '         ' + fields[i].typeDeclaration + ' ' + fields[i].fieldName + (i === fields.length - 1 ? ') ' : ',\n')
    }

    // output constructor content
    result += '{\n\n'
    for (i = 0; i < fields.length; i++) {
      result += '        this.' + fields[i].fieldName + ' = ' + fields[i].fieldName + ';\n'
    }
    result += '    }\n\n\n'

    if (isGetSet) {
      // output public getters
      for (i = 0; i < fields.length; i++) {
        const javaGetterName = (fields[i].typeDeclaration === 'Boolean' ? 'is' : 'get') + capitalize(fields[i].fieldName)
        result += '    ' + 'public ' + fields[i].typeDeclaration + ' ' + javaGetterName + '() {\n        return ' + fields[i].fieldName + ';\n    }\n' + (i === fields.length - 1 ? '' : '\n')
      }

      result += '\n\n\n\n'

      // output public setters
      for (i = 0; i < fields.length; i++) {
        result += '    ' + 'public void set' + capitalize(fields[i].fieldName) + '(' + fields[i].typeDeclaration + ' ' + fields[i].fieldName + ') {\n        this.' + fields[i].fieldName + ' = ' + fields[i].fieldName + ';\n    }\n' + (i === fields.length - 1 ? '' : '\n')
      }
    }
    result += '}\n\n\n'

    return result
  }

  instance.convert = function (json, isGetSet, isApi) {
    const objectDefinition = getObjectDefinition(JSON.parse(json))
    try {
    } catch (ex) {
      return ex
    }

    const classQueue = [
      {
        'name': 'RootClass',
        'definition': objectDefinition
      }
    ]

    let result = ''

    if (!isGetSet) {
      result += 'import lombok.Data;\n'
    }
    if (isApi) {
      result += 'import io.swagger.annotations.ApiModelProperty;\n'
    }
    result += '\n'
    if (!isGetSet) {
      result += '@Data\n'
    }

    while (classQueue.length > 0) {
      const fields = []
      const cls = classQueue.shift()

      for (const field in cls.definition) {
        const type = cls.definition[field].type
        let arrayType = ''
        let objType = undefined

        if (type === 'array') {
          if (cls.definition[field].definition.type === 'object') {
            classQueue.push({
              'name': capitalize(camelCase(field)) + 'ItemType',
              'definition': cls.definition[field].definition.definition
            })
            arrayType = '<' + capitalize(camelCase(field)) + 'ItemType>'
          } else {
            arrayType = '<' + capitalize(cls.definition[field].definition.type) + '>'
          }
        }

        if (type === 'object') {
          objType = capitalize(camelCase(field)) + 'Type'
          classQueue.push({
            'name': objType,
            'definition': cls.definition[camelCase(field)].definition
          })
        }

        const typeDeclaration = objType || getJavaType(type) + arrayType

        fields.push({
          'fieldName': camelCase(field),
          'fieldJson': field,
          'typeDeclaration': typeDeclaration
        })
      }
      result += getJavaClassDefinition(cls.name, fields, isGetSet, isApi)
    }

    return result
  }

  return instance
}

export default jsonToPojoConverter
