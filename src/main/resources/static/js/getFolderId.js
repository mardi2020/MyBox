function passOnData() {
    console.log("button: ", $("#idButton"));
    $('button').on('click', function() {
        console.log("find input");
        var path = $(this).data('path');
        var id = $(this).data('id');

        console.log("path :", path);
        console.log("id: ", id);
        //$(this).parents('tr').next('#createFolderForm').find('#parent').val(path);
        $("#filePath").val(path);
        $("#parent").val(path);
        $("#parentId").val(id);

        // document.getElementById("parent").value = path;
        // document.getElementById("parentId").value = id;
    })
}

function passOnData2() {
    var folderName = $("#folderName")[0].value
    var parent = $("#parent")[0].value
    var parentId = $("#parentId")[0].value
    const formData = new FormData();

    formData.append("filePath", parent);
    formData.append("folder", folderName)
    formData.append("parentId", parentId);
    $.ajax({
        type: "POST",
        url: "/createFolder",
        processData: false,
        contentType: false,
        data: formData,
        success: function (rtn) {
            console.log("message: ", rtn)
        },
        err: function (err) {
            console.log("err:", err)
        }
    })
}