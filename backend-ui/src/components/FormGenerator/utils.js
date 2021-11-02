import _ from 'lodash'
import moment from 'moment'

export const parseValueType = (name, value, fields) => {
  const type = fields['valueType'] || null

  const ret = []
  switch (type) {
    case 'int':
      if (value !== '') {
        value = _parseInt(value)
      } else {
        value = null
      }
      break
    case 'string':
      if (_.isEmpty(value)) {
        value = value + ''
        break
      }
      if (_.isArray(value)) {
        value = value.join(',')
      } else if (_.isObject(value)) {
        value = JSON.stringify(value)
      } else {
        value = value + ''
      }
      break
    case 'stringIndex_1':
      if (_.isEmpty(value)) {
        break
      }
      if (_.isArray(value)) {
        value = value[0]
      } else if (_.isObject(value)) {
        const arr = _.values(value)
        value = arr[0]
      } else {
        value = value + ''
      }
      break
    case 'bool':
      if (value == null || value === '') {
        value = null
      } else {
        value = value >= 1
      }
      break
    case 'float':
      value = parseFloat(value)
      break
    case 'array':
      if (value == null || value === '' || (_.isObject(value) && value.length === 0)) {
        value = null
      } else if (_.isString(value) || _.isNumber(value)) {
        value = value.split(',')
      } else if (_.isObject(value)) {
        const ret = []
        _.each(value, (val) => {
          ret.push(val)
        })
        value = ret
      }
      break
    case 'arrayIndex_1':
      if (value == null || value === '' || (_.isObject(value) && value.length === 0)) {
        value = null
      } else if (_.isString(value) || _.isNumber(value)) {
        value = value.split(',')
      } else if (_.isObject(value)) {
        const ret = []
        _.each(value, (val) => {
          ret.push(val)
        })
        value = ret
      }
      if (value != null) {
        value = value[0]
      }
      break
    case 'arrayInt':

      if (value == null || value === '' || (_.isObject(value) && value.length === 0)) {
        value = null
      } else if (_.isArray(value) || _.isObject(value)) {
        _.each(value, (val) => {
          const valueKey = fields['valueKey'] || null
          if (_.isObject(val) && valueKey != null) {
            ret.push(_parseInt(_.get(val, valueKey)))
          } else {
            ret.push(_parseInt(val))
          }
        })
        value = ret
      } else if (_.isString(value)) {
        value = value.split(',')
        _.each(value, (val) => {
          ret.push(_parseInt(val))
        })
        value = ret
      } else {
        value = [_parseInt(value)]
      }
      break
    case 'arrayFloat':

      if (value == null || value === '' || (_.isObject(value) && value.length === 0)) {
        value = null
      } else if (_.isArray(value) || _.isObject(value)) {
        _.each(value, (val) => {
          ret.push(parseFloat(val))
        })
        value = ret
      } else if (_.isString(value)) {
        value = value.split(',')
        _.each(value, (val) => {
          ret.push(parseFloat(val))
        })
        value = ret
      } else {
        value = [parseFloat(value)]
      }
      break
    case 'arrayString':

      if (value == null || value === '' || (_.isObject(value) && value.length === 0)) {
        value = null
      } else if (_.isArray(value) || _.isObject(value)) {
        _.each(value, (val) => {
          const valueKey = fields['valueKey'] || null
          if (_.isObject(val) && valueKey != null) {
            ret.push(_.get(val, valueKey) + '')
          } else {
            ret.push(val + '')
          }
        })
        value = ret
      } else if (_.isString(value)) {
        value = value.split(',')
      } else {
        value = [value + '']
      }
      break
    case 'objectPath':
      const valuePath1 = fields['valuePath'] || null
      value = value[valuePath1] || null
      break
    case 'objectPathToArray':
      const valuePath2 = fields['valuePath'] || null

      if (_.isObject(value)) {
        _.each(value, (val) => {
          ret.push(val[valuePath2])
        })
        value = ret
      }
      break
    case 'JSONString':
      if (_.isArray(value) || _.isObject(value)) {
        value = JSON.stringify(value)
      }
      break

    default:
  }

  return value
}

function _parseInt (val) {
  if (val === undefined || val === '') {
    val = null
  } else {
    val = parseInt(val)
  }
  return val
}

export const functionCallback = (callback) => {
  if (_.isFunction(callback)) {
    return callback
  }

  if (_.isString(callback) && callback.indexOf('function') > -1) {
    // eslint-disable-next-line no-new-func
    const fun = new Function('return ' + callback)
    return fun()
  }

  return (val) => {}
}

export const formatValue = (fields, name, value, values) => {
  let _value = value

  if (!_.isEmpty(value)) {
    if (_.isArray(value) && value.length === 2 && moment.isMoment(value[0]) && moment.isMoment(value[1])) {
      let format = 'YYYY-MM-DD'
      if (fields['format'] !== undefined) {
        format = fields['format']
      }

      const start = value[0].toString()
      const end = value[1].toString()

      _value = []
      _value[0] = moment(start).format(format).toString()
      _value[1] = moment(end).format(format).toString()
    } else if (moment.isMoment(value)) {
      let format = 'YYYY-MM-DD'
      if (fields['format'] !== undefined) {
        format = fields['format']
      } else if (fields['type'] === 'time-picker') {
        format = fields['format'] ? fields['format'] : 'hh:mm:ss'
      }

      _value = moment(value).format(format).toString()
    } else {
      _value = value
    }
  } else {
    _value = value
  }
  const type = _.has(fields, 'valueType') ? fields['valueType'] : null
  if (type !== null) {
    console.log('======>', type, _value)
    _value = parseValueType(name, _value, fields)
  }

  const valueCallback = _.has(fields, 'valueCallback') ? fields['valueCallback'] : null
  if (valueCallback) {
    _value = functionCallback(valueCallback)(_value, fields, values)
  }

  return _value
}
