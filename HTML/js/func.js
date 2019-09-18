
/**
获取对象的属性值
*/
function getStyle(obj,attr)
{
	if(obj.currentStyle)
		return obj.currentStyle[attr];
	else
		return getComputedStyle(obj)[attr]
}
/**
使指定的对象进行移动
obj：要移动的对象
attr：对象的属性（left,top,height,width....）
speed：移动的速度
target：移动的目标点
func:回调函数
*/
function doMove(obj,attr,speed,target,func)
{
	clearInterval(obj.timer);
	speed = parseInt(getStyle(obj,attr))>target?-speed:speed ;
	console.log("speed:"+speed);
	obj.timer = setInterval(function()
	{
		var position = parseInt(getStyle(obj,attr))+speed ;
		console.log("position:"+position);
		if((speed>0 && position>target) || (speed<0 && position<target ) )
		{
			position = target ;
		}
		if(position == target)
		{
			clearInterval(obj.timer);
			if(func)
				func();
		}
		obj.style[attr]=position+'px';
	},10);
}

/**
使物体抖动
*/
function shake(obj,attr,func)
{
	var pos = parseInt( getStyle(obj, attr) );
	var arr = [];		
	var num = 0;
	var timer = null;
	for ( var i=20; i>=0; i-=2 ) {
		arr.push( i, -i );
	}
	clearInterval( obj.shake );
	obj.shake = setInterval(function ()
	{
		obj.style[attr] = pos + arr[num] + 'px';
		num++;
		if ( num === arr.length ) 
		{
			clearInterval( obj.shake );
			endFn && endFn();
		}
	}, 50);
}