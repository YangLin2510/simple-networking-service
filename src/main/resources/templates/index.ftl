<html>
<head>
    <link rel="stylesheet" href="https://cdn.bootcss.com/bootstrap/4.0.0/css/bootstrap.min.css"
          integrity="sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm" crossorigin="anonymous">
    <style>
        .conatct-list {
            list-style: none;

        }

        #message-display-box {
            height: 410px
        }

        #message-send-box {
            border-top: 1px solid
        }

        #message {width: 100%;height: 40px;}
    </style>
</head>
<body>
<div class="container">
    <h1 style="display: block">网络即时消息</h1>


    <div class="row" style="border: 1px solid">
        <div class="col-sm-4" style="border-right: 1px solid;height: 450px;">
            <p>通讯列表</p>
            <hr>
            <div>
                <ul class="conatct-list">
                    <li>A</li>
                    <li>B</li>
                    <li>C</li>
                </ul>
            </div>
        </div>
        <div class="col-sm-8">
            <div class="row" id="message-display-box">

            </div>

            <div id="message-send-box" class="row">
                <div class="col-sm-8" style="padding: 0;margin: 0;"><input id="message" style="font-size: x-large"/></div>
                <div class="col-sm-4"><input type="button" class="btn btn-default btn-block" value="发送"/></div>
            </div>
        </div>
    </div>

</div>
</body>
<#--<script src="https://cdn.bootcss.com/bootstrap/4.0.0/js/bootstrap.min.js"
        integrity="sha384-JZR6Spejh4U02d8jOt6vLEHfe/JQGiRRSQQxSfFWpi1MquVdAyjUar5+76PVCmYl"
        crossorigin="anonymous"></script>-->

<script>
    var ws = new WebSocket("ws://localhost:9001/chat");
    ws.onopen = function (evt) {
        console.log("Connection open ...");
        ws.send("Hello WebSockets!");
    };

    ws.onmessage = function (evt) {
        console.log("Received Message: " + evt.data);
        ws.close();
    };

    ws.onclose = function (evt) {
        console.log("Connection closed.");
    };
</script>
</html>