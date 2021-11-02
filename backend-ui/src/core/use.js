import Vue from 'vue'
import config from '@/config/defaultSettings'

// base library
import Antd from 'ant-design-vue'
import Viser from 'viser-vue'
import VueCropper from 'vue-cropper'
import 'ant-design-vue/dist/antd.less'

// ext library
import VueClipboard from 'vue-clipboard2'
import PermissionAuth from '@/utils/permission'
// import '@/components/use'
import './directives/permission'
import splitPane from '@/components/split-pane/index.vue'

import simplebar from 'simplebar-vue'
import 'simplebar/dist/simplebar.min.css'

import FormGenerator from '@/components/FormGenerator/FormGenerator'
import FormItemGenerator from '@/components/FormGenerator/FormItemGenerator'
import SFormItemGenerator from '@/components/FormGenerator/SFormItemGenerator'
import SearchFormGenerator from '@/components/FormGenerator/SearchFormGenerator'
import GroupFormGenerator from '@/components/FormGenerator/GroupFormGenerator'
import EditTable from '@/components/Table/EditTable'

VueClipboard.config.autoSetContainer = true

Vue.use(Antd)
Vue.use(Viser)

Vue.use(VueClipboard)
Vue.use(PermissionAuth)
Vue.use(VueCropper)

Vue.component('split-pane', splitPane)
Vue.component('form-generator', FormGenerator)
Vue.component('search-form-generator', SearchFormGenerator)
Vue.component('form-item-generator', FormItemGenerator)
Vue.component('s-form-item-generator', SFormItemGenerator)
Vue.component('group-form-generator', GroupFormGenerator)
Vue.component('edit-table', EditTable)
