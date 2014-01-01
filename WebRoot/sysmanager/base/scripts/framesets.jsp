
<jsp:include page="panes.jsp" /> 
<script language="javascript">
  function getActionsContainerFrameset()
  {   
    return perspectiveContent.frames["base_actions_container"].document.getElementsByTagName("frameset").item[0];
  }

  function processFramesetSizes(framesetSizesForm)
  {
    toggleDoubleClickColumnTitle();
    var perspectiveContentFrameset = getPerspectiveContentFrameset();
    var actionsContainerFrameset = getActionsContainerFrameset();   
    framesetSizesForm.framesetColsPerspectiveContent.value = perspectiveContentFrameset.cols;
    framesetSizesForm.framesetRowsActionsContainer.value = actionsContainerFrameset.rows;
    framesetSizesForm.submit();
  }
</script>
