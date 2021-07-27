(window["webpackJsonp"]=window["webpackJsonp"]||[]).push([["chunk-4d5f3d06"],{2978:function(t,e,s){"use strict";var i=s("db72"),a=s("9256"),r={page:"/authority/systemApi/page",update:{method:"PUT",url:"/authority/systemApi"},save:{method:"POST",url:"/authority/systemApi"},delete:{method:"DELETE",url:"/authority/systemApi"}};e["a"]={page:function(t){return Object(a["a"])({method:"GET",url:r.page,formData:!0,data:t})},save:function(t){return Object(a["a"])(Object(i["a"])({},r.save,{data:t}))},update:function(t){return Object(a["a"])(Object(i["a"])({},r.update,{data:t}))},delete:function(t){return Object(a["a"])(Object(i["a"])({},r.delete,{data:t}))}}},"464c":function(t,e,s){"use strict";var i=s("9321"),a=s.n(i);a.a},"4cef":function(t,e,s){"use strict";s.r(e);var i=function(){var t=this,e=t.$createElement,s=t._self._c||e;return s("el-dialog",{attrs:{"close-on-click-modal":!1,"close-on-press-escape":!0,title:t.title,type:t.type,visible:t.isVisible,width:t.width,top:"50px"},on:{"update:visible":function(e){t.isVisible=e}}},[s("el-form",{ref:"form",attrs:{model:t.systemApi,rules:t.rules,"label-position":"right","label-width":"100px"}},[s("el-form-item",{attrs:{label:t.$t("table.systemApi.serviceId"),prop:"serviceId"}},[s("el-select",{staticStyle:{width:"100%"},attrs:{disabled:"view"===t.type,placeholder:"",value:""},model:{value:t.systemApi.serviceId,callback:function(e){t.$set(t.systemApi,"serviceId",e)},expression:"systemApi.serviceId"}},t._l(t.serviceList,(function(t){return s("el-option",{key:t.id,attrs:{label:t.name,value:t.id}})})),1)],1),t._v(" "),s("el-form-item",{attrs:{label:t.$t("table.systemApi.code"),prop:"code"}},[s("el-input",{attrs:{disabled:"view"===t.type||"edit"===t.type},model:{value:t.systemApi.code,callback:function(e){t.$set(t.systemApi,"code",e)},expression:"systemApi.code"}})],1),t._v(" "),s("el-form-item",{attrs:{label:t.$t("table.systemApi.name"),prop:"name"}},[s("el-input",{attrs:{disabled:"view"===t.type},model:{value:t.systemApi.name,callback:function(e){t.$set(t.systemApi,"name",e)},expression:"systemApi.name"}})],1),t._v(" "),s("el-form-item",{attrs:{label:t.$t("table.systemApi.path"),prop:"path"}},[s("el-input",{attrs:{disabled:"view"===t.type},model:{value:t.systemApi.path,callback:function(e){t.$set(t.systemApi,"path",e)},expression:"systemApi.path"}})],1),t._v(" "),s("el-form-item",{attrs:{label:t.$t("table.systemApi.status"),prop:"status"}},[s("el-radio-group",{attrs:{border:"true",size:"small"},model:{value:t.systemApi.status,callback:function(e){t.$set(t.systemApi,"status",e)},expression:"systemApi.status"}},[s("el-radio-button",{attrs:{label:!0}},[t._v(t._s(t.$t("common.status.valid")))]),t._v(" "),s("el-radio-button",{attrs:{label:!1}},[t._v(t._s(t.$t("common.status.invalid")))])],1),t._v(" "),s("aside",{staticClass:"tips"},[t._v('禁用：提示"请求地址,禁止访问!";')])],1),t._v(" "),s("el-form-item",{attrs:{label:t.$t("table.systemApi.isAuth"),prop:"isAuth"}},[s("el-radio-group",{attrs:{size:"small"},model:{value:t.systemApi.isAuth,callback:function(e){t.$set(t.systemApi,"isAuth",e)},expression:"systemApi.isAuth"}},[s("el-radio-button",{attrs:{label:!0}},[t._v(t._s(t.$t("common.yes")))]),t._v(" "),s("el-radio-button",{attrs:{label:!1}},[t._v(t._s(t.$t("common.no")))])],1),t._v(" "),s("aside",{staticClass:"tips"},[t._v('是：未认证登录,提示"认证失败,请重新登录!";否: 不需要认证登录')])],1),t._v(" "),s("el-form-item",{attrs:{label:t.$t("table.systemApi.describe"),prop:"describe"}},[s("el-input",{model:{value:t.systemApi.describe,callback:function(e){t.$set(t.systemApi,"describe",e)},expression:"systemApi.describe"}})],1)],1),t._v(" "),s("div",{staticClass:"dialog-footer",attrs:{slot:"footer"},slot:"footer"},[s("el-button",{attrs:{plain:"",type:"warning"},on:{click:function(e){t.isVisible=!1}}},[t._v(t._s(t.$t("common.cancel")))]),t._v(" "),s("el-button",{attrs:{plain:"",type:"primary"},on:{click:t.submitForm}},[t._v(t._s(t.$t("common.confirm")))])],1)],1)},a=[],r=s("db72"),n=(s("542c"),s("2978")),o={name:"SystemApiEdit",components:{},props:{dialogVisible:{type:Boolean,default:!1},type:{type:String,default:"add"}},data:function(){return{remoteSystemApiLoading:!1,systemApi:this.initSystemApi(),screenWidth:0,width:this.initWidth(),orgList:[],systemApiList:[],rules:{name:[{required:!0,message:this.$t("rules.require"),trigger:"blur"},{min:1,max:255,message:this.$t("rules.range4to10"),trigger:"blur"}],status:{required:!0,message:this.$t("rules.require"),trigger:"blur"},serviceId:[{required:!0,message:this.$t("rules.require"),trigger:"blur"},{min:1,max:50,message:this.$t("rules.range4to10"),trigger:"blur"}]},serviceList:[{id:"pd-auth-server",name:"权限服务"}]}},computed:{isVisible:{get:function(){return this.dialogVisible},set:function(){this.close(),this.reset()}},title:function(){return"add"===this.type?this.$t("common.add"):this.$t("common.edit")}},watch:{},mounted:function(){var t=this;window.onresize=function(){return function(){t.width=t.initWidth()}()}},methods:{initSystemApi:function(){return{id:"",code:"",describe:"",requestMethod:"",contentType:"",serviceId:"",path:"",status:!0,isOpen:!0,isAuth:!1}},initWidth:function(){return this.screenWidth=document.body.clientWidth,this.screenWidth<991?"90%":this.screenWidth<1400?"45%":"800px"},loadListOptions:function(t){var e=t.callback;e()},setSystemApi:function(t,e){var s=this;s.orgList=e,t&&(s.systemApi=Object(r["a"])({},t))},close:function(){this.$emit("close")},reset:function(){this.$refs.form.clearValidate(),this.$refs.form.resetFields(),this.systemApi=this.initSystemApi()},submitForm:function(){var t=this;this.$refs.form.validate((function(e){if(!e)return!1;t.editSubmit()}))},editSubmit:function(){var t=this;"add"===t.type?t.save():t.update()},save:function(){var t=this;n["a"].save(this.systemApi).then((function(e){var s=e.data;s.isSuccess&&(t.isVisible=!1,t.$message({message:t.$t("tips.createSuccess"),type:"success"}),t.$emit("success"))}))},update:function(){var t=this;n["a"].update(this.systemApi).then((function(e){var s=e.data;s.isSuccess&&(t.isVisible=!1,t.$message({message:t.$t("tips.updateSuccess"),type:"success"}),t.$emit("success"))}))}}},l=o,c=(s("464c"),s("2877")),u=Object(c["a"])(l,i,a,!1,null,"4978137c",null);e["default"]=u.exports},"542c":function(t,e,s){},9321:function(t,e,s){}}]);