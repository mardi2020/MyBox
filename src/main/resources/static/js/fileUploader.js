function upload() {
    console.log($("#fileInput"))
    const fileInput = $("#fileInput")[0];

    const parentId = $("#parentId").val();
    console.log("parentId", parentId);

    // 파일을 여러개 선택할 수 있으므로 files 라는 객체에 담긴다.
    console.log("fileInput: ", fileInput.files)

    if (fileInput.files.length === 0) {
        alert("파일은 선택해주세요");
        return;
    }
    const filePath = $("#filePath").val();
    const formData = new FormData();
    formData.append("uploadFile", fileInput.files[0]);
    formData.append("uploadFileName", fileInput.files[0].name)
    formData.append("filePath", filePath);
    formData.append("parentId", parentId);

    $.ajax({
        type: "POST",
        url: "/upload",
        processData: false,
        contentType: false,
        data: formData,
        success: function (rtn) {
            const message = rtn.data.values[0];
            console.log("message: ", message)
            $("#resultUploadPath").text(message.uploadFilePath)
        },
        err: function (err) {
            console.log("err:", err)
        }
    })
}