
import { dictAll } from '@/modules/system/api/dict'
// import RGB from '@/utils/RGB'
import { colorById } from '@/utils/util'
import storage from 'store'
import _ from 'lodash'

const core = {
  state: {
    dict: {}
  },

  mutations: {
    SET_DICT: (state, dict) => {
      storage.set('DICT', dict)

      const res = []

      _.each(dict, (item) => {
        _.each(item.dictDetails, (detail, i) => {
          if (detail['color'] == '') {
            detail['color'] = colorById(i + 30)
          }

          if (!res[item.type]) {
            res[item.type] = []
          }
          res[item.type].push(detail)
        })
      })

      console.log(res)

      state.dict = res
    }
  },

  actions: {

    DictAll ({ commit }) {
      return new Promise((resolve, reject) => {
        dictAll().then(response => {
          const { items } = response.data

          commit('SET_DICT', items)

          resolve(response)
        }).catch(error => {
          reject(error)
        })
      })
    }

  }
}

export default core
