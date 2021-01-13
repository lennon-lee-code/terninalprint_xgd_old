
// login、functions、cardList、ownCardList、billList 页面通用注销方法

var load_time = null;
var down_time = null;

//打开页面30分钟不操作就跳转
load_time = setTimeout(logout, 1800000);

document.onmousedown = function() {
	// 停止时钟load_time
	clearInterval(load_time);
	if (null != down_time) {
		// 停止时钟down_time
		clearInterval(down_time);
	}
	// 30分钟不操作就跳转
	down_time = setTimeout(logout, 1800000);
}

function logout(){
	$.ajax({
		type : "post",
		url : "doLogout",
		success : function(data){
			window.location.href = "/printing";
		},
		error : function(err){
			console.log(err);
		}
	});
}