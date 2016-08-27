function switchSysBar() {
    if(switchPoint.innerText==3) {
        switchPoint.innerText = 4;
        document.all("mnuList").style.display = "none";
        top.content.cols = "16,*";
    }
    else {
        switchPoint.innerText = 3;
        document.all("mnuList").style.display = "";
        top.content.cols = "166,*";
    }
}

function showitem(id,name,target,item) {
	
    return ("<span><a href='"+id+"' target='" + target +"' alt=''>"+name+"</a></span><br>");
}

function showpage(pageurl,target,firsted,name) {
	
	
	if(firsted)
	{
    	return ("<span><iframe class='contentdiv1' name='" + name + "' scrolling=no STYLE='background-color: #B9CEF0' allowTransparency='true' FRAMEBORDER='no' border=0 hidefocus=true src='" + pageurl + "'></iframe></span><br>");
    	}
    else
    {
    	var ret = ("<span><iframe scrolling=auto class='contentdiv1' name='" + name + "' scrolling=no STYLE='background-color: #B9CEF0' allowTransparency='true' FRAMEBORDER='no' border=0 hidefocus=true theurl=" + pageurl + "></iframe></span><br>");

    	return ret;
    	}
    
}
 
function switchoutlookBar(number) {
	//记录先前的打开的outlookbar
    var i = outlookbar.opentitle;
    //设置当前点击的outlookbar
    outlookbar.opentitle=number;
    
    //alert("11");
    //alert(number);
    var id1,id2,id1b,id2b;
    if(number!=i && outlooksmoothstat==0) {
    
    	//检测页面是否是已经装载过,如果还未装载,则装载,然后标识outlookbar已经装载过.
    	
    	if(outlookbar.titlelist[number].pages.length > 0 && !(outlookbar.titlelist[number].firsted))
    	{    		
    		var pages = outlookbar.titlelist[number].pages;
    		for(var k = 0; k < pages.length; k ++)
    		{
    			var pid = "outlookitem" + number + pages[k];
    			document.all.item(pid).src = document.all.item(pid).theurl;
    		}
    		outlookbar.titlelist[number].firsted = true;
    	}
        if(number!=-1) {
            if(i==-1) {
                id2 = "blankdiv";
                id2b = "blankdiv";
            }
            else {
                id2 = "outlookdiv" + i;
                id2b = "outlookdivin" + i;
                //document.all("outlooktitle"+i).style.border = "0px none navy";
               
                //document.all("outlooktitle"+i).style.color = "white";
                //document.all("outlooktitle"+i).style.textalign = "center";
            }
            id1 = "outlookdiv" + number;
            id1b= "outlookdivin" + number;
            //document.all("outlooktitle"+number).style.border = "0px none white";
            
            //document.all("outlooktitle"+number).style.color = "white";
            //document.all("outlooktitle"+number).style.textalign = "center";
            
            smoothout(id1,id2,id1b,id2b,0);
        }
        else {
            document.all("blankdiv").style.display = "";
            document.all("blankdiv").sryle.height = "100%";
            document.all("outlookdiv"+i).style.display = "none";
            document.all("outlookdiv"+i).style.height = "0%";
            //document.all("outlooktitle"+i).style.border = "0px none navy";
            
           // document.all("outlooktitle"+i).style.color = "white";
            //document.all("outlooktitle"+i).style.textalign = "center";
        }
    }
}
function smoothout(id1,id2,id1b,id2b,stat) {
	
    if(stat==0) {
     //biaoping.yin注释开始 on 2008.07.29 10:44
 
        //tempinnertext1=document.all(id1b).innerHTML;
        
        //tempinnertext2=document.all(id2b).innerHTML;
       
        //document.all(id1b).innerHTML = "";
       // document.all(id2b).innerHTML = "";
       //biaoping.yin注释结束 on 2008.07.29 10:44
        outlooksmoothstat = 1;
        document.all(id1b).style.overflow = "hidden";
        document.all(id2b).style.overflow = "hidden";
        document.all(id1).style.height = "0%";
        document.all(id1).style.display = "";     
        
        setTimeout("smoothout('"+id1+"','"+id2+"','"+id1b+"','"+id2b+"',"+outlookbar.inc+")",outlookbar.timedalay);
    }
    else {
    
        stat+=outlookbar.inc;
        if(stat>100)
            stat = 100;
        document.all(id1).style.height = stat + "%";
        document.all(id2).style.height = (100-stat) + "%";
        if(stat<100)
            setTimeout("smoothout('"+id1+"','"+id2+"','"+id1b+"','"+id2b+"',"+stat+")",outlookbar.timedalay);
        else {
        //biaoping.yin注释开始 on 2008.07.29 10:44
            //document.all(id1b).innerHTML = tempinnertext1;
           // document.all(id2b).innerHTML = tempinnertext2;
           //biaoping.yin注释结束 on 2008.07.29 10:44
            outlooksmoothstat = 0;
            document.all(id1b).style.overflow = "auto";
            document.all(id2).style.display = "none";
        }
        
    }
}
function getOutLine() {
    outline = "<table border='0' cellpadding='0' cellspacing='0'"+outlookbar.otherclass+">";
    if(outlookbar.navigator)
    {
    	outline += "<tr><td align='center'><span><a href='"+outlookbar.navigator.key+"' target='" + outlookbar.navigator.target +"' alt='"+ outlookbar.navigator.title + "'>"+outlookbar.navigator.title+"</a></span></td></tr>"
    }
    for(i=0;i<(outlookbar.titlelist.length);i++) {
        //outline+="<tr><td background='sysmanager/style/blue/images/leftnav01.jpg' name=outlooktitle" + i + " id=outlooktitle" + i + " ";
    	outline+="<tr><td class='leftHead' name=outlooktitle" + i + " id=outlooktitle" + i +" ";
        if(i!=outlookbar.opentitle)
            outline+=" nowrap align=center style='cursor:hand;background-color:none;color:white;height:10;border:0 none navy' ";
        else
            outline+=" nowrap align=center style='cursor:hand;background-color:none;color:white;height:10;border:0 none white' ";
        outline+=outlookbar.titlelist[i].otherclass;
        outline+=" onclick='switchoutlookBar(" + i + ")'>";
        
        var h_img = outlookbar.titlelist[i].state;
        if(h_img && h_img != 'null' && h_img != '')
        {
        	
        	outline+= "<img src='" + h_img + "' border=0>&nbsp;&nbsp;"
        }
        outline+="<span class=smallFont >";
        outline+=outlookbar.titlelist[i].title + "</span></td></tr>";
        outline+="<tr><td name=outlookdiv" + i + " valign=top align=center id=outlookdiv"+i+" style='width:100%";
        
        if(i!=outlookbar.opentitle)
        {
            outline+=";display:none;height:0%;";
        }
        else
        {
        	
        	outlookbar.titlelist[i].firsted = true;
            outline+=";display:;height:100%;";
            
		}
        outline+="'><div name=outlookdivin"+i+" id=outlookdivin"+i+" style='overflow:auto;width:100%;height:100%'>";
        for(j=0;j<outlookbar.itemlist[i].length;j++)
        {
        	
        	var item = outlookbar.itemlist[i][j];
        	if(item instanceof theitem)
        	{
        		
            	outline+=showitem(item.key,item.title,item.target);
            	
            }
            else if(item instanceof pageitem)
            {
            	
            	
        		var itemid = "outlookitem" + i + j;
        		//alert(outlookbar.titlelist[i].firsted);
        		var str = showpage(item.pageurl,item.target,outlookbar.titlelist[i].firsted,itemid);
        		
        		outline += str;
        		
        		//alert(outlookbar.titlelist[i].firsted);
            	
            }
            
        }
        outline+="</div></td></tr>";
    }
     if(outlookbar.navigator)
     {
    	
    	outline += "<tr><td align='center' height='4'><span><a href='"+outlookbar.navigator.key+"' target='" + outlookbar.navigator.target +"'>"+outlookbar.navigator.title+"</a></span></td></tr>"
     }
    outline+="</table>";
    return outline;
}
function show() {
    var outline;
    outline = "<div id=outLookBarDiv name=outLookBarDiv style='width=100%;height:100%'>";
    outline+=outlookbar.getOutLine();
    outline+="</div>";
    document.write(outline);
}

/**
 * The parameter 'instate" has been used to set the head image of outlookbar title
 */
function theitem(intitle,instate,inkey,target) {
    this.state = instate;
    this.otherclass = " nowrap ";
    this.key = inkey;
    this.title = intitle;
    this.target = target;
    this.firsted = false;  
    //存放title中存放的pages index
    this.pages = new Array();  
}

/**
 * The parameter 'instate" has been used to set the head image of outlookbar title
 */
function pageitem(pageurl,target) {
    
    this.otherclass = " nowrap ";
    this.pageurl = pageurl;
    this.target = target;
    
    
    
}
function addNavigator(intitle,url,target)
{
	outlookbar.navigator = new theitem(intitle,0,url,target);
}

/**
 * Added parameter 'head_img' for setting head image of outlookbar title on 2006/9/27 by biaoping.yin
 */
function addtitle(head_img,intitle) {
    outlookbar.itemlist[outlookbar.titlelist.length] = new Array();
    outlookbar.titlelist[outlookbar.titlelist.length] = new theitem(intitle,head_img,0);
    return(outlookbar.titlelist.length-1);
}
function additem(intitle,parentid,inkey,target) {
    if(parentid>=0 && parentid<=outlookbar.titlelist.length) {
        outlookbar.itemlist[parentid][outlookbar.itemlist[parentid].length]=new theitem(intitle,2,inkey,target);
        
        outlookbar.itemlist[parentid][outlookbar.itemlist[parentid].length-1].otherclass=" nowrap align=left style='height:5' ";
        
        return(outlookbar.itemlist[parentid].length-1);
    }
    else
        additem=-1;
}

function additempage(parentid,inkey,target) {
    if(parentid>=0 && parentid<=outlookbar.titlelist.length) {
        outlookbar.itemlist[parentid][outlookbar.itemlist[parentid].length]=new pageitem(inkey,target);
        //保存pages index
        outlookbar.titlelist[parentid].pages.push(outlookbar.itemlist[parentid].length - 1);
        
        outlookbar.itemlist[parentid][outlookbar.itemlist[parentid].length-1].otherclass=" nowrap align=left style='height:100%' ";
        return(outlookbar.itemlist[parentid].length-1);
    }
    else
        additem=-1;
}
function outlook() {
    this.titlelist = new Array();
    this.itemlist = new Array();
    this.divstyle = "style='height:100%;width:100%;overflow:auto' align=center";
    this.otherclass = "border=0 cellspacing='0' cellpadding='0' style='padding:4;height:100%;width:100%'valign=middle align=center";
    this.addtitle = addtitle;
    this.addNavigator = addNavigator;
    this.navigator = null;
    this.additem = additem;
    this.starttitle = -1;
    this.show = show;
    this.getOutLine = getOutLine;
    this.opentitle = this.starttitle;
    this.reflesh = outreflesh;
    this.timedelay = 50;
    this.inc = 10;
    this.additempage = additempage;
}
function outreflesh() {
    document.all("outLookBarDiv").innerHTML = outlookbar.getOutLine();
}
function locatefold(foldname) {
    for(var i=0;i<outlookbar.titlelist.length;i++)
        if(foldname==outlookbar.titlelist[i].title) {
            outlookbar.starttitle = i;
            outlookbar.opentitle = i;
        }
}
var outlookbar = new outlook();
var tempinnertext1,tempinnertext2,outlooksmoothstat;
outlooksmoothstat = 0;
