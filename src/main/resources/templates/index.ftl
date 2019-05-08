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
            background:rgba(244,68,68,0.9);
        }
        .conatct-list li:hover:not(.selected) {
            background:gray;
        }
        #message-display-box {
            height: 410px;
            overflow: auto;
        }

        #message-send-box {
            border-top: 1px solid
        }

        #message {width: 100%;height: 40px;}

        .sendTo {text-align: right}

        .datetime {font-size: x-small}
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
                    <li v-for="item in contactList" :key="item.id" :class="{selected : item.id == currentContactId}" @click="choiceContact(item)">{{item.name}}</li>
                </ul>
            </div>
        </div>
        <div class="col-sm-8">
            <div class="row" id="message-display-box">
                <div class="col-sm-12">
                    <p v-for="item in messageList" :class="{sendTo : item.type=='sendTo'}">
                        {{item.msg}}
                    </p>

                </div>

            </div>

            <div id="message-send-box" class="row">
                <div class="col-sm-8" style="padding: 0;margin: 0;"><input id="message" style="font-size: x-large" v-model="messageWaitSend" @keydown.down.13='sendMsg'/></div>
                <div class="col-sm-4"><input type="button" class="btn btn-default btn-block" value="发送" @click="sendMsg"/></div>
            </div>
        </div>
    </div>

</div>
</body>
<#--<script src="https://cdn.bootcss.com/bootstrap/4.0.0/js/bootstrap.min.js"
        integrity="sha384-JZR6Spejh4U02d8jOt6vLEHfe/JQGiRRSQQxSfFWpi1MquVdAyjUar5+76PVCmYl"
        crossorigin="anonymous"></script>-->
<script src="https://cdn.jsdelivr.net/npm/vue@2.6.10/dist/vue.js"></script>

<script>
    Date.prototype.Format = function(fmt)
    { //author: meizz
        var o = {
            "M+" : this.getMonth()+1,                 //月份
            "d+" : this.getDate(),                    //日
            "h+" : this.getHours(),                   //小时
            "m+" : this.getMinutes(),                 //分
            "s+" : this.getSeconds(),                 //秒
            "q+" : Math.floor((this.getMonth()+3)/3), //季度
            "S"  : this.getMilliseconds()             //毫秒
        };
        if(/(y+)/.test(fmt))
            fmt=fmt.replace(RegExp.$1, (this.getFullYear()+"").substr(4 - RegExp.$1.length));
        for(var k in o)
            if(new RegExp("("+ k +")").test(fmt))
                fmt = fmt.replace(RegExp.$1, (RegExp.$1.length==1) ? (o[k]) : (("00"+ o[k]).substr((""+ o[k]).length)));
        return fmt;
    };


    var vm = new Vue({
        el:"#app",
        created(){
            var ws = new WebSocket("ws://localhost:9001/chat");
            ws.onopen = function (evt) {
                console.log("Connection open ...");
                ws.send("Hello WebSockets!");
            };

            ws.onmessage = function (evt) {
                console.log("Received Message: " + evt.data);
                var msgObj = {"type":"receiveFrom","msg":evt.data};
                vm.receiveMsg(msgObj);
            };

            ws.onclose = function (evt) {
                console.log("Connection closed.");
            };
        },
        data : {
            messageWaitSend:"",
            messageList:[],
            contactList : [{"id":"1","name":"测试1"},{"id":"2","name":"测试2"}],
            currentContactId:""
        },
        methods : {
            sendMsg:function () {
                if(this.messageWaitSend === '')
                    return
                //消息显示到消息框
                var dateTime = (new Date()).Format("hh:mm:ss")
                var msg = {"type":"sendTo","msg":this.messageWaitSend,"dateTime":dateTime};
                this.messageList.splice(this.messageList.length,0,msg);
                this.messageWaitSend = "";

                //发送消息到消息队列

            },
            receiveMsg : function (msgObj) {
                if(msgObj == undefined)
                    return
                this.messageList.splice(this.messageList.length,0,msgObj);
            },
            choiceContact : function (item) {
                this.currentContactId = item.id;
            }
        }
    });
</script>


</html>