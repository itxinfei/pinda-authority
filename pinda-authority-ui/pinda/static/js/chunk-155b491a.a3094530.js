(window["webpackJsonp"]=window["webpackJsonp"]||[]).push([["chunk-155b491a"],{"5e69":function(s,t,e){"use strict";var i=e("88bc"),a=e.n(i);a.a},"6ac5":function(s,t,e){"use strict";e.r(t);var i=function(){var s=this,t=s.$createElement,e=s._self._c||t;return e("el-dialog",{staticClass:"user-view",attrs:{title:s.$t("common.view"),visible:s.isVisible,width:s.width},on:{"update:visible":function(t){s.isVisible=t}}},[e("el-row",{attrs:{gutter:10}},[e("el-col",{attrs:{sm:24,xs:24}},[e("div",{staticClass:"img-wrapper"},[e("el-avatar",{key:s.user.avatar,attrs:{src:s.user.avatar,fit:"fill"}},[e("el-avatar",[s._v(s._s(s._f("userAvatarFilter")(s.user.name)))])],1)],1)])],1),s._v(" "),e("el-row",{attrs:{gutter:10}},[e("el-col",{attrs:{sm:12,xs:24}},[e("div",{staticClass:"view-item"},[e("i",{staticClass:"el-icon-user"}),s._v(" "),e("span",[s._v(s._s(s.$t("table.user.account")+"："))]),s._v("\n        "+s._s(s.user.account)+"\n      ")])]),s._v(" "),e("el-col",{attrs:{sm:12,xs:24}},[e("div",{staticClass:"view-item"},[e("i",{staticClass:"el-icon-suitcase"}),s._v(" "),e("span",[s._v(s._s(s.$t("table.user.name")+"："))]),s._v("\n        "+s._s(s.user.name)+"\n      ")])])],1),s._v(" "),e("el-row",{attrs:{gutter:10}},[e("el-col",{attrs:{sm:12,xs:24}},[e("div",{staticClass:"view-item"},[e("i",{staticClass:"el-icon-user"}),s._v(" "),e("span",[s._v(s._s(s.$t("table.user.email")+"："))]),s._v("\n        "+s._s(s.user.email)+"\n      ")])]),s._v(" "),e("el-col",{attrs:{sm:12,xs:24}},[e("div",{staticClass:"view-item"},[e("i",{staticClass:"el-icon-suitcase"}),s._v(" "),e("span",[s._v(s._s(s.$t("table.user.mobile")+"："))]),s._v("\n        "+s._s(s.user.mobile)+"\n      ")])])],1),s._v(" "),e("el-row",{attrs:{gutter:10}},[e("el-col",{attrs:{sm:12,xs:24}},[e("div",{staticClass:"view-item"},[e("i",{staticClass:"el-icon-trophy"}),s._v(" "),e("span",[s._v(s._s(s.$t("table.user.orgId")+"："))]),s._v("\n        "+s._s(s.user.orgId)+"\n      ")])]),s._v(" "),e("el-col",{attrs:{sm:12,xs:24}},[e("div",{staticClass:"view-item"},[e("i",{staticClass:"el-icon-phone-outline"}),s._v(" "),e("span",[s._v(s._s(s.$t("table.user.stationId")+"："))]),s._v("\n        "+s._s(s.user.stationId)+"\n      ")])])],1),s._v(" "),e("el-row",{attrs:{gutter:10}},[e("el-col",{attrs:{sm:12,xs:24}},[e("div",{staticClass:"view-item"},[e("i",{staticClass:"el-icon-star-off"}),s._v(" "),e("span",[s._v(s._s(s.$t("table.user.sex")+"："))]),s._v("\n        "+s._s(s.user.sex.desc)+"\n      ")])]),s._v(" "),e("el-col",{attrs:{sm:12,xs:24}},[e("div",{staticClass:"view-item"},[e("i",{staticClass:"el-icon-bangzhu"}),s._v(" "),e("span",[s._v(s._s(s.$t("table.user.status")+"："))]),s._v("\n        "+s._s(s.user.status?s.$t("common.status.valid"):s.$t("common.status.invalid"))+"\n      ")])])],1),s._v(" "),e("el-row",{attrs:{gutter:10}},[e("el-col",{attrs:{sm:12,xs:24}},[e("div",{staticClass:"view-item"},[e("i",{staticClass:"el-icon-time"}),s._v(" "),e("span",[s._v(s._s(s.$t("table.user.createTime")+"："))]),s._v("\n        "+s._s(s.user.createTime)+"\n      ")])]),s._v(" "),e("el-col",{attrs:{sm:12,xs:24}},[e("div",{staticClass:"view-item"},[e("i",{staticClass:"el-icon-brush"}),s._v(" "),e("span",[s._v(s._s(s.$t("table.user.updateTime")+"："))]),s._v("\n        "+s._s(s.user.updateTime)+"\n      ")])])],1),s._v(" "),e("el-row",{attrs:{gutter:10}},[e("el-col",{attrs:{sm:12,xs:24}},[e("div",{staticClass:"view-item"},[e("i",{staticClass:"el-icon-date"}),s._v(" "),e("span",[s._v(s._s(s.$t("table.user.lastLoginTime")+"："))]),s._v("\n        "+s._s(s.user.lastLoginTime?s.user.lastLoginTime:s.$t("tips.neverLogin"))+"\n      ")])]),s._v(" "),e("el-col",{attrs:{sm:12,xs:24}},[e("div",{staticClass:"view-item"},[e("i",{staticClass:"el-icon-date"}),s._v(" "),e("span",[s._v(s._s(s.$t("table.user.passwordExpireTime")+"："))]),s._v("\n        "+s._s(s.user.passwordExpireTime)+"\n      ")])])],1),s._v(" "),e("el-row",{attrs:{gutter:10}},[e("el-col",{attrs:{sm:12,xs:24}},[e("div",{staticClass:"view-item"},[e("i",{staticClass:"el-icon-date"}),s._v(" "),e("span",[s._v(s._s(s.$t("table.user.passwordErrorLastTime")+"："))]),s._v("\n        "+s._s(s.user.passwordErrorLastTime)+"\n      ")])]),s._v(" "),e("el-col",{attrs:{sm:12,xs:24}},[e("div",{staticClass:"view-item"},[e("i",{staticClass:"el-icon-date"}),s._v(" "),e("span",[s._v(s._s(s.$t("table.user.passwordErrorNum")+"："))]),s._v("\n        "+s._s(s.user.passwordErrorNum)+"\n      ")])])],1),s._v(" "),e("el-row",{attrs:{gutter:10}},[e("el-col",{attrs:{sm:24,xs:24}},[e("div",{staticClass:"view-item"},[e("i",{staticClass:"el-icon-document"}),s._v(" "),e("span",[s._v(s._s(s.$t("table.user.workDescribe")+"："))]),s._v("\n        "+s._s(s.user.workDescribe?s.user.workDescribe:s.$t("tips.nothing"))+"\n      ")])])],1)],1)},a=[],r=e("db72"),l={name:"UserView",filters:{userAvatarFilter:function(s){return s?s.charAt(0):"无"}},props:{dialogVisible:{type:Boolean,default:!1}},data:function(){return{screenWidth:0,width:this.initWidth(),user:{sex:{desc:""}}}},computed:{isVisible:{get:function(){return this.dialogVisible},set:function(){this.close()}}},mounted:function(){var s=this;window.onresize=function(){return function(){s.width=s.initWidth()}()}},methods:{initWidth:function(){return this.screenWidth=document.body.clientWidth,this.screenWidth<550?"95%":this.screenWidth<990?"580px":this.screenWidth<1400?"600px":"650px"},setUser:function(s){this.user=Object(r["a"])({},s)},close:function(){this.$emit("close")}}},n=l,c=(e("5e69"),e("2877")),o=Object(c["a"])(n,i,a,!1,null,"229a3e50",null);t["default"]=o.exports},"88bc":function(s,t,e){}}]);