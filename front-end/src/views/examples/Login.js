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
  Button,
  Card,
  CardBody,
  FormGroup,
  Form,
  Input,
  InputGroupAddon,
  InputGroupText,
  InputGroup,
  Row,
  Col,
} from "reactstrap";
import {login} from "../../api/user";
import {useState} from "react";
import { Link } from 'react-router-dom';

const Login = () => {
  const [inputEmail, setInputEmail] = useState('');
  const [inputPassword, setInputPassword] = useState('');
  const [isLoginFailed, setIsLoginFailed] = useState(false);

  const handleInputId = (e) => {
    setInputEmail(e.target.value);
  }

  const handleInputPassword = (e) => {
    setInputPassword(e.target.value);
  }

  const onClickLoginBtn = async () => {
    try {
      await login(inputEmail, inputPassword);
      sessionStorage.setItem("isLoggedIn", "true");
      window.location.reload();
    } catch (e) {
      sessionStorage.removeItem("isLoggedIn");
      setIsLoginFailed(true);
    }
  }

  return (
    <>
      <Col lg="5" md="7">
        <Card className="bg-secondary shadow border-0">
          <CardBody className="px-lg-5 py-lg-5">
            <div className="text-center text-muted mb-4">
              <small>로그인하기</small>
            </div>
            <Form role="form">
              <FormGroup className="mb-3">
                <InputGroup className="input-group-alternative">
                  <InputGroupAddon addonType="prepend">
                    <InputGroupText>
                      <i className="ni ni-email-83" />
                    </InputGroupText>
                  </InputGroupAddon>
                  <Input placeholder="이메일" type="email"  value={inputEmail} onChange={handleInputId} autoComplete="new-email"/> {/* Email */}
                </InputGroup>
              </FormGroup>
              <FormGroup>
                <InputGroup className="input-group-alternative">
                  <InputGroupAddon addonType="prepend">
                    <InputGroupText>
                      <i className="ni ni-lock-circle-open" />
                    </InputGroupText>
                  </InputGroupAddon>
                  <Input placeholder="비밀번호" type="password" value={inputPassword} onChange={handleInputPassword} autoComplete="new-password"/> {/* password */}
                </InputGroup>
              </FormGroup>

              <div className="text-center">
                {isLoginFailed && (<p>Login Unsuccessful</p>)}
                <Button className="my-4" color="primary" type="button" onClick={onClickLoginBtn}>로그인</Button>
              </div>
            </Form>
          </CardBody>
        </Card>
        <Row className="mt-3">
          <Col xs="6">
            <Link
              className="text-light"
              to="/auth/forgot-password"
            >
              <small>비밀번호 분실했다면?</small>
            </Link>
          </Col>
          <Col className="text-right" xs="6">
            <Link
              className="text-light"
              to="/auth/register"
            >
              <small>회원 가입</small>
            </Link>
          </Col>
        </Row>
      </Col>
    </>
  );
};

export default Login;
