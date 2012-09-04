//
// <copyright>
//
// Licensed Material - Property of IBM
// (C) Copyright IBM Corp. 2000, 2002 - All Rights Reserved.
// US Government Users Restricted Rights - Use, duplication or disclosure
// restricted by GSA ADP Schedule Contract with IBM Corp.
//
// </copyright>
//
// File plugins/com.ibm.etools.webservice.explorer/wsexplorer/scripts/toolbar.js, wsa.etools.ws.explorer, lunar, 20030717a 1
// Version 1.1 03/02/28 15:19:08
//
var globalCheckButton;
function mouseover(img)
{
    img.className = "hover";
}

function mouseout(img)
{
  if(globalCheckButton && globalCheckButton == img)
        img.className = "down";
  else
        img.className = "normal";
}

function mousedown(img)
{
    if(globalCheckButton)
        globalCheckButton.className = "normal";
    globalCheckButton = img;
    img.className = "down";
}

function mouseup(img)
{
  return mouseout(img);
}
