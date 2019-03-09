function login() {
    var span=$("#errorMsg");
    span.css({'color':"red",'fontSize':'12px'});
    $("[for='username']").text("");
    $("[for='password']").text("");
    var username=$("[name='username']").val();
    if(username==""){
        $("[for='username']").text("用户名不得为空");
        return;
    }

    var password=$("[name='password']").val();
    if(password==""){
        $("[for='password']").text("密码不得为空");
        return;
    }

    var vCode=$("[name='vCode']").val();
    if(vCode==""){
        span.text('验证码不能为空!')
        return;
    }

    var data=$("form").serialize();
    $.post("/hello/login",data,function (data) {
         var json=eval("("+data+")")
         if(json.success){
             location.href="/hello/toMain";
         }else{
             if(json.message=="用户名不存在"){
                 $("[for='username']").text(json.message);
             }else if(json.message=="密码错误"){
                 $("[for='password']").text(json.message);
             }else if(json.message=="验证码错误!"){
                 span.text(json.message);
             }else if(json.message=="请点击邮件激活链接！"){
                 span.text(json.message);
             }
         }
    })
}

function changeImg() {
    var img=$("#validateCode");
    var date=new Date();
    img.attr('src',"/hello/getGifCode?"+date.getTime());
}