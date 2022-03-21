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
  Card,
  CardHeader,
  CardBody,
  FormGroup,
  Form,
  Input,
  Container,
  Row,
  Col, Button,
} from "reactstrap";
// core components
import {useEffect, useState} from "react";
import client from "../../api/client";
import { resetPassword } from "../../api/user";

const Profile = () => {
  const [user, setUser] = useState(null);
  const [password, setPassword] = useState("");
  const [checkPassword, setCheckPassword] = useState(""); // password와 같아야함
  const [nowPassword, setNowPassword] = useState("");
  const [message, setMessage] = useState("");

  useEffect(() => {
    client.get("http://192.168.0.26:8080/user/myInfo")
        .then(response => {
          setUser(response.data);
        }).catch(e => {
          console.error(e);
    })
      }, []
  )
  const handleInputPassword = (e) => {
    setPassword(e.target.value);
  }

  const handleNowPassword = (e) => {
    setNowPassword(e.target.value)
  }
  const handleInputCheckPassword = (e) => {
    setCheckPassword(e.target.value);
  }

  const isPasswordEqual = () => {
    if (password === checkPassword)
      return true;
    else
      return false;
  }

  const onClickChangePassword = () => {
    if (isPasswordEqual())
      resetPassword(user.email, nowPassword, password).then(res => {
        setMessage(res.data);
      })
    else {
      setMessage("변경할 비밀번호와 재확인이 일치하지않습니다.")
    }
  }

  return (
    <>
      <div
          className="header bg-gradient-info pb-8 pt-5 pt-md-8"
      >
        {/* Mask */}
        <span className="mask bg-gradient-default opacity-8" />
        {/* Header container */}
        <Container className="d-flex align-items-center" fluid>
          <Row>
            <Col lg="15" md="10">
              <h1 className="display-2 text-white">My profile page</h1>
              <p className="text-white mt-0 mb-5">
                저장된 정보를 수정하거나 탈퇴할 수 있습니다. &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
              </p>
              <Button
                  color="info"
                  href="#pablo"
                  onClick={onClickChangePassword}
              >
                프로필 수정하기
              </Button>
            </Col>
          </Row>
        </Container>
      </div>
      {/* Page content */}
      <Container className="mt--7" fluid>
        <Row>
          <Col className="order-xl-1" xl="8">
            <Card className="bg-secondary shadow">
              <CardHeader className="bg-white border-0">
                <Row className="align-items-center">
                  <Col xs="8">
                    <h3 className="mb-0">나의 정보</h3>
                  </Col>
                </Row>
                <Row className="align-items-center">
                  <Col xs="8">
                    <h3 className="mb-0" style={{color: 'red'}}>{message}</h3>
                  </Col>
                </Row>
              </CardHeader>
              <CardBody>
                <Form>
                  <h6 className="heading-small text-muted mb-4">
                    User information
                  </h6>
                  <div className="pl-lg-4">
                    <Row>
                      <Col lg="6">
                        <FormGroup>
                          <label
                            className="form-control-label"
                            htmlFor="input-username"
                          >
                            유저 이름
                          </label>
                          <Input
                            className="form-control-alternative"
                            id="input-username"
                            type="text"
                            defaultValue={user?.userName}
                          readOnly/>
                        </FormGroup>
                      </Col>
                      <Col lg="6">
                        <FormGroup>
                          <label
                            className="form-control-label"
                            htmlFor="input-email"
                          >
                            이메일 주소
                          </label>
                          <Input
                            className="form-control-alternative"
                            defaultValue={user?.email}
                            readOnly> </Input>
                        </FormGroup>
                      </Col>
                    </Row>
                    <Row>
                      <Col lg="6">
                        <FormGroup>
                          <label
                              className="form-control-label"
                              htmlFor="input-username"
                          >
                            기존 비밀번호
                          </label>
                          <Input
                              className="form-control-alternative"
                              id="input-username"
                              type="password"
                              value={nowPassword}
                              onChange={handleNowPassword}
                          />
                        </FormGroup>
                      </Col>
                      <Col lg="6">
                        <FormGroup>
                          <label
                              className="form-control-label"
                              htmlFor="input-email"
                          >
                            변경할 비밀번호
                          </label>
                          <Input
                              className="form-control-alternative"
                              id="input-email"
                              type="password"
                              value={password}
                              onChange={handleInputPassword}
                              />
                        </FormGroup>
                      </Col>
                    </Row>
                    <Row>
                      <Col lg="6">
                        <FormGroup>
                          <label
                              className="form-control-label"
                              htmlFor="input-email"
                          >
                            비밀번호 재확인
                          </label>
                          <Input
                              className="form-control-alternative"
                              id="input-email"
                              type="password"
                              value={checkPassword}
                              onChange={handleInputCheckPassword}
                          />
                        </FormGroup>
                      </Col>
                    </Row>
                  </div>
                  <hr className="my-4" />
                  <h6 className="heading-small text-muted mb-4">
                    사용량
                  </h6>
                  <div className="pl-lg-4">
                  <Row>
                    <Col lg="6">
                      <FormGroup>
                        <label
                            className="form-control-label"
                            htmlFor="input-username"
                        >
                          총 할당량
                        </label>
                        <Input
                            className="form-control-alternative"
                            defaultValue={user?.maxSize}
                            type="text"
                        readOnly/>
                      </FormGroup>
                    </Col>
                    <Col lg="6">
                      <FormGroup>
                        <label
                            className="form-control-label"
                            htmlFor="input-email"
                        >
                          총 사용량
                        </label>
                        <Input
                            className="form-control-alternative"
                            type="text"
                            defaultValue={user?.currentSize}
                        readOnly/>
                      </FormGroup>
                    </Col>
                  </Row>
                  </div>
                </Form>
              </CardBody>
            </Card>
          </Col>
        </Row>
      </Container>
    </>
  );
};

export default Profile;
