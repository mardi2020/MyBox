/*!

=========================================================
* Argon Dashboard React - v1.2.1
=========================================================

* Product Page: https://www.creative-tim.com/product/argon-dashboard-react
* Copyright 2021 Creative Tim (https://www.creative-tim.com)
* Licensed under MIT (https://github.com/creativetimofficial/argon-dashboard-react/blob/master/LICENSE.md)

* Coded by Creative Tim

=========================================================

* The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

*/
// reactstrap components
import {
  Badge,
  Card, CardHeader,
    Input,
    Modal,
  DropdownMenu,
  DropdownItem,
  UncontrolledDropdown,
  DropdownToggle,
  Media,
  Table,
  Container,
  Row, Button, Col,
} from "reactstrap";
// core components
import Header from "components/Headers/Header.js";
import { AiOutlineDownload } from "react-icons/ai";
import { GoFileDirectory, GoFile } from "react-icons/go";
import {useEffect, useState} from "react";
import {getFileList, getFileIdByPath, uploadFile, deleteDirectory, deleteFile, createNewDirectory, renameFile } from "../../api/file";

const Tables = () => {
  const [files, setFiles] = useState(null);
  const [path, setPath] = useState("");
  const [selectedFile, setSelectedFile] = useState(null);
  const [selectedFileName, setSelectedFileName] = useState("");
  const [parentId, setParentId] = useState("");
  const [exampleModal, setExampleModal] = useState(false);
  const [uploadModal, setUploadModal] = useState(false);
  const [changeNameModal, setChangeNameModal] = useState(false);
  const [changeFileName, setChangeFileName] = useState("");
  const [changeName, setChangeName] = useState(""); // 디렉토리 생성용 이름 변경해야됨!!

  const [changingFileId, setChangingFileId] = useState("");

  useEffect(() => {
    console.log("파일 정보", files);
  },[files])

  useEffect(() => {
    console.log(selectedFile);
  },[selectedFile])

  useEffect(() => {
    console.log("현재 경로" + path);
      getFileList(path, setFiles)
  }, [path])

  const toggleModal = () => {
    setExampleModal(prev => !prev)
  }

  const toggleUploadModal = () => {
    setUploadModal(prev => !prev);
  }

  const toggleChangeModal = (id) => {
    if (id) {
      setChangingFileId(id);
    }
    setChangeNameModal(prev => !prev)
  }

  const handleChangeFileName = (e) => {
    setChangeFileName(e.target.value)
  }
  const handleFileInput = e => {
    setSelectedFile(e.target.files[0]);
    setSelectedFileName(e.target.files[0].name);
  }

  const handleClickFileUpload = () => {
    // selectedFile
    uploadFile(selectedFileName, selectedFile, path, parentId).then(() => {
      getFileList(path, setFiles)
    })
  }

  const handleChangeName = (e) => {
    setChangeName(e.target.value)
  }

  const onClickChangeFileNameBtn = () => {
    renameFile()
  }

  const onClickChangeNameBtn = () => {
    createNewDirectory(path, changeName, parentId).then(() => {
      getFileList(path, setFiles)
    })
  }

  useEffect(() => {
    getFileIdByPath(path, setParentId)
    console.log("parentId = ", parentId)
  },[path])

  const pathSplit = path.split('/');
  const prevPage = pathSplit.slice(0, pathSplit.length - 1).join('/');

  return (
    <>
      <Header />
      {/* Page content */}
      <Container className="mt--7" fluid>
        {/* Table */}
        <Row>
          <div className="col">
            <Card className="shadow">
              <CardHeader className="border-0">
                <Row className="align-items-center">
                <Col xs="8">
                  <h3 className="mb-0">나의 저장 공간</h3>
                </Col>
                  {path &&
                    <Col className="text-right" xs="4">
                      <Button
                          color="default"
                          size="sm"
                          name="fileBtn"
                          onClick={() => toggleUploadModal()}
                      >
                        파일 추가
                      </Button>
                      {/* Modal */}
                      <Modal
                          className="modal-dialog-centered"
                          isOpen={uploadModal}
                          toggle={() => toggleUploadModal()}
                      >
                        <div className="modal-header">
                          <h5 className="modal-title" id="exampleModalLabel">
                            디렉토리 생성
                          </h5>
                          <button
                              aria-label="Close"
                              className="close"
                              data-dismiss="modal"
                              type="button"
                              onClick={() => toggleUploadModal()}
                          >
                            <span aria-hidden={true}>×</span>
                          </button>
                        </div>
                        <div className="modal-body">
                          <input id="uploadFile" name="uploadFile" type="file" onChange={handleFileInput}/>
                        </div>
                        <div className="modal-footer">
                          <Button
                              color="secondary"
                              data-dismiss="modal"
                              type="button"
                              onClick={() => toggleUploadModal()}
                          >
                            닫기
                          </Button>
                          <Button color="primary" type="button" onClick={handleClickFileUpload}>
                            생성
                          </Button>
                        </div>
                      </Modal>
                      <Button
                          color="default"
                          href="#pablo"
                          size="sm"
                          onClick={() => toggleModal()}
                      >
                        폴더 생성
                      </Button>
                      {/* Modal */}
                      <Modal
                          className="modal-dialog-centered"
                          isOpen={exampleModal}
                          toggle={() => toggleModal()}
                      >
                        <div className="modal-header">
                          <h5 className="modal-title" id="exampleModalLabel">
                            디렉토리 생성
                          </h5>
                          <button
                              aria-label="Close"
                              className="close"
                              data-dismiss="modal"
                              type="button"
                              onClick={() => toggleModal()}
                          >
                            <span aria-hidden={true}>×</span>
                          </button>
                        </div>
                        <div className="modal-body">
                          <Input id="input-changeName" className="input-changeName" type="text" value={changeName}
                                 onChange={handleChangeName}/>
                        </div>
                        <div className="modal-footer">
                          <Button
                              color="secondary"
                              data-dismiss="modal"
                              type="button"
                              onClick={() => toggleModal()}
                          >
                            닫기
                          </Button>
                          <Button color="primary" type="button" onClick={onClickChangeNameBtn}>
                            생성
                          </Button>
                        </div>
                      </Modal>
                    </Col>
                  }
                </Row>
              </CardHeader>
              <Table className="align-items-center table-flush" responsive>
                <thead className="thead-light">
                  <tr>
                    <th scope="col">Name</th>
                    <th scope="col">Size</th>
                    <th scope="col">Date</th>
                    <th scope="col">Download</th>
                    <th scope="col" />
                  </tr>
                </thead>
                <tbody>
                {path &&
                  <tr>
                    <th scope="row">
                      <Media className="align-items-center">
                        <Row>
                          <a
                              color="default"
                              href="#"
                              onClick={() => (setPath(prevPage))}
                          >
                            <GoFileDirectory size={30}/>
                            <Media>
                                            <span className="mb-0 text-sm">
                                              ...
                                            </span>
                            </Media>
                          </a>
                        </Row>
                      </Media>
                    </th>
                    <td/>
                    <td/>
                    <td/>
                    <td className="text-right"/>
                  </tr>
                }
                {files?.files?.length && (
                    files?.files?.map(file => {
                      return (
                          <tr key={`table-row-${file.id}`}>
                            <th scope="row">
                              <Media className="align-items-center">
                                  {file.directory ?
                                      (file.root ?
                                              <Row>
                                        <a
                                          color="default"
                                          href="#"
                                          onClick={() => (setPath(file.path + file.fileName))}
                                        >
                                          <GoFileDirectory size={30}/>
                                          <Media>
                                            <span className="mb-0 text-sm">
                                              {file.fileName}
                                            </span>
                                          </Media>
                                        </a></Row>: <a
                                                color="default"
                                                href="#"
                                                onClick={() => (setPath(file.path + "/" + file.fileName))}
                                              >
                                                <GoFileDirectory size={30}/>
                                                <Media>
                                            <span className="mb-0 text-sm">
                                              {file.fileName}
                                            </span>
                                                </Media>
                                            </a>
                                      ): <a color="default">
                                          <GoFile size={30}/>
                                        <Media>
                                            <span className="mb-0 text-sm">
                                              {file.fileName}
                                            </span>
                                        </Media>
                                        </a>
                                  }
                              </Media>
                            </th>
                            <td>{file.fileSize}</td>
                            <td>
                              <Badge color="" className="badge-dot mr-4">
                                {file.createdDate}
                              </Badge>
                            </td>
                            <td>
                              {!file.directory &&
                                (
                                    <a href={`http://192.168.0.26:8080/download/${file.id.toString()}`}>
                                      <AiOutlineDownload size={20} />
                                    </a>
                                )}
                            </td>

                            <td className="text-right">
                              {!file.root &&
                                <UncontrolledDropdown>
                                  <DropdownToggle
                                      className="btn-icon-only text-light"
                                      href="#pablo"
                                      role="button"
                                      size="sm"
                                      color=""
                                      onClick={(e) => e.preventDefault()}
                                  >
                                    <i className="fas fa-ellipsis-v"/>
                                  </DropdownToggle>
                                  <DropdownMenu className="dropdown-menu-arrow" right>
                                    {file.directory ?
                                        <DropdownItem
                                            href="#pablo"
                                            onClick={() => (deleteDirectory(file.id))}
                                        >
                                          삭제
                                        </DropdownItem>
                                        : <DropdownItem
                                            href="#pablo"
                                            onClick={() => (deleteFile(file.id))}
                                        >
                                          삭제
                                        </DropdownItem>
                                    }
                                    {!file.directory &&
                                    <DropdownItem
                                        href="#pablo"
                                        onClick={() => (toggleChangeModal(file.id))}
                                    >
                                      이름 변경
                                      {/* Modal */}
                                      <Modal
                                          className="modal-dialog-centered"
                                          isOpen={changeNameModal}
                                          toggle={() => toggleChangeModal()}
                                      >
                                        <div className="modal-header">
                                          <h5 className="modal-title" id="exampleModalLabel">
                                            이름 수정하기
                                          </h5>
                                          <button
                                              aria-label="Close"
                                              className="close"
                                              data-dismiss="modal"
                                              type="button"
                                              onClick={() => toggleChangeModal()}
                                          >
                                            <span aria-hidden={true}>×</span>
                                          </button>
                                        </div>
                                        <div className="modal-body">
                                          <Input type="text" onChange={handleChangeFileName}/>
                                        </div>
                                        <div className="modal-footer">
                                          <Button
                                              color="secondary"
                                              data-dismiss="modal"
                                              type="button"
                                              onClick={() => toggleChangeModal()}
                                          >
                                            닫기
                                          </Button>
                                          <Button color="primary" type="button"
                                                  onClick={() => (renameFile(changingFileId, changeFileName))}>
                                            수정하기
                                          </Button>
                                        </div>
                                      </Modal>
                                    </DropdownItem>
                                    }
                                  </DropdownMenu>
                                </UncontrolledDropdown>
                              }
                            </td>
                          </tr>
                      )
                    })
                )}

                </tbody>
              </Table>

            </Card>
          </div>
        </Row>

      </Container>
    </>
  );
};

export default Tables;
