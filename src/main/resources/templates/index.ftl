<html>
<head>
    <link rel="stylesheet" href="https://cdn.bootcss.com/bootstrap/4.0.0/css/bootstrap.min.css"
          integrity="sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm" crossorigin="anonymous">
    <style>
        .conatct-list {
            list-style: none;
            padding: 0;
            margin: 0;
        }

        .conatct-list li {
            display: block;
            margin-bottom: 5px;
            cursor: pointer;
        }

        .selected {
            background: rgba(244, 68, 68, 0.9);
        }

        .conatct-list li:hover:not(.selected) {
            background: gray;
        }

        #message-display-box {
            height: 410px;
            overflow: auto;
        }

        #message-send-box {
            border-top: 1px solid
        }

        #message {
            width: 100%;
            height: 40px;
        }

        .sendTo {
            text-align: right
        }

        .datetime {
            font-size: x-small
        }

        /* 解决模板删现问题*/
        [v-cloak] {
            display: none;
        }
    </style>
</head>
<body>
<div class="container" id="app">
    <h1 style="display: block">网络即时消息</h1>


    <div class="row" style="border: 1px solid">
        <div class="col-sm-4" style="border-right: 1px solid;height: 450px;text-align: center">
            <p>通讯列表</p>
            <hr>
            <div>
                <ul class="conatct-list">
                    <li v-cloak v-for="item in contactList" :key="item.id"
                        :class="{selected : item.id == currentContactId}" @click="choiceContact(item)">{{item.username}}
                    </li>
                </ul>
            </div>
        </div>
        <div class="col-sm-8">
            <div class="row" id="message-display-box" v-show="currentContactId !==''">
                <div class="col-sm-12">
                    <p v-cloak v-for="item in msgList" :class="{sendTo : item.type=='sendTo'}">
                        {{item.msg}}
                    </p>

                </div>

            </div>

            <div id="message-send-box" class="row" v-show="currentContactId !==''">
                <div class="col-sm-8" style="padding: 0;margin: 0;"><input id="message" style="font-size: x-large"
                                                                           v-model="messageWaitSend"
                                                                           @keydown.down.13='sendMsg'/></div>
                <div class="col-sm-4"><input type="button" class="btn btn-default btn-block" value="发送"
                                             @click="sendMsg"/></div>
            </div>
        </div>
    </div>

</div>
</body>
<#--<script src="https://cdn.bootcss.com/bootstrap/4.0.0/js/bootstrap.min.js"
        integrity="sha384-JZR6Spejh4U02d8jOt6vLEHfe/JQGiRRSQQxSfFWpi1MquVdAyjUar5+76PVCmYl"
        crossorigin="anonymous"></script>-->
<script src="https://cdn.jsdelivr.net/npm/vue@2.6.10/dist/vue.js"></script>
<script src="https://unpkg.com/axios/dist/axios.min.js"></script>

<script>
    Date.prototype.Format = function (fmt) { //author: meizz
        var o = {
            "M+": this.getMonth() + 1,                 //月份
            "d+": this.getDate(),                    //日
            "h+": this.getHours(),                   //小时
            "m+": this.getMinutes(),                 //分
            "s+": this.getSeconds(),                 //秒
            "q+": Math.floor((this.getMonth() + 3) / 3), //季度
            "S": this.getMilliseconds()             //毫秒
        };
        if (/(y+)/.test(fmt))
            fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
        for (var k in o)
            if (new RegExp("(" + k + ")").test(fmt))
                fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
        return fmt;
    };


    var vm = new Vue({
        el: "#app",
        created() {

        },
        beforeMount: function () {
            //获取用户信息
            axios.get("/user/info").then(function (resp) {
                var data = resp.data;
                if (data.code == '0') {
                    var contactList = data.data.contactList.split(",");
                    vm.user = data.data;
                    vm.init_websocket();
                    axios.get("/init/"+vm.user.id);//设置消息队列的监听器
                    axios.post("/user/contact_list", contactList).then(function (r) {
                        var cc = r.data.data;
                        vm.contactList = cc;
                    })
                }
            });
        },
        data: {
            messageWaitSend: "",//输入框内容
            messageList: [],//消息列表
            contactList: [],//联系人列表
            currentContactId: "",//当前联系人ID
            user: {},//当前登录的用户信息
        },
        computed: {
            //通过联系人，过滤消息
            msgList: function () {
                var f = this.messageList.filter(function (value) {
                    //是接受的消息类型，且发送人是当前选择的联系人
                    if (value.type == 'receiveFrom' && value.sendId == vm.currentContactId) {//接受的消息
                        return true;
                    }
                    //是发送消息类型，且当前联系人是接受人
                    if (value.type == 'sendTo' && value.receiveId == vm.currentContactId) {
                        return true;
                    }
                    return false;
                });
                return f;
            }
        },
        methods: {
            sendMsg: function () {
                if (this.messageWaitSend === '')
                    return
                //消息显示到消息框
                var dateTime = (new Date()).Format("hh:mm:ss")
                var msg = {
                    "receiveId": this.currentContactId,
                    "type": "sendTo",
                    "msg": this.messageWaitSend,
                    "dateTime": dateTime
                };
                this.messageList.splice(this.messageList.length, 0, msg);


                //发送消息到消息队列
                axios.post("/sendToQueue",{"username":this.user.id,"msg":this.messageWaitSend,"receiver":this.currentContactId,"sendDateTime":(new Date()).Format("yyyy-MM-dd hh:mm:ss")}).then(function (value) {
                    vm.messageWaitSend = "";//消息发送成功后再清除输入框
                })
            },
            receiveMsg: function (msgObj) {
                if (msgObj == undefined)
                    return
                msgObj = eval("(" + msgObj + ")")
                var msg = {"type":"receiveFrom","msg":msgObj.msg,"sendId":msgObj.username,"dateTime":msgObj.sendDateTime};
                this.messageList.splice(this.messageList.length, 0, msg);
            },
            choiceContact: function (item) {
                this.currentContactId = item.id;
            },
            init_websocket: function () {
                var ws = new WebSocket("ws://localhost:9001/chat/" + vm.user.id);
                ws.onopen = function (evt) {
                    console.log("Connection open ...");
                    ws.send("Hello WebSockets!");
                };

                ws.onmessage = function (evt) {
                    vm.receiveMsg(evt.data);
                };

                ws.onclose = function (evt) {
                    console.log("Connection closed.");
                };
            }
        }
    });
</script>


</html>