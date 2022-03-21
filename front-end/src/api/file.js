import client from "./client";

// upload file
export const uploadFile = (uploadFileName, uploadFile, filePath, parentId) => {
    console.log(uploadFile);
    const formData = new FormData();
    formData.append('uploadFile', uploadFile);
    formData.append('uploadFileName', uploadFileName);
    formData.append('filePath', filePath);
    formData.append('parentId', parentId);

    return client.post("http://192.168.0.26:8080/file/upload", formData)
        .then(function (response){}).
        catch(function (error) {})
}
// create directory
export const createNewDirectory = async (path, directoryName, parentId) => {
    return await client.post("http://192.168.0.26:8080/file/directory", {
        path,
        directoryName,
        parentId
    })
        .then(function (response) {
        }).catch(function (error) {
        });
}

// delete file
export const deleteFile = (objectId) => {
    client.delete("http://192.168.0.26:8080/file/" + objectId, {
    }).then(function (response){
    }).catch(function (error) {})
}
// delete directory
export const deleteDirectory = (objectId) => {
    client.delete("http://192.168.0.26:8080/file/directory/" + objectId, {
    }).then(function (response){
    }).catch(function (error) {})
}

// rename file
export const renameFile = async (objectId, fileName) => {
    console.log(objectId)
    const response = await client.patch("http://192.168.0.26:8080/file/" + objectId, {
        fileName
    }).then(function(response){
    }).catch(function (error) {})
}

// show file list in path
export const getFileList = async (path, setFiles) => {
    const response = await client.get("http://192.168.0.26:8080/file", {
        params: {
            path: path
        }
    })

    setFiles(response.data);
}

// file path로 file의 objectId 가져오기
export const getFileIdByPath = async (path, setData) => {
    const splitPath = path.split('/');
    const inputPath = splitPath.slice(0, splitPath.length - 1).join('/');
    const inputName = splitPath[splitPath.length - 1];
    console.log(inputPath, inputName)
    const response = await client.get("http://192.168.0.26:8080/file/directory", {
        params: {
            name: inputName,
            path: inputPath
        }
    })

    setData(response.data);
}

