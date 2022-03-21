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
  Col,
} from "reactstrap";
import { register } from "../../api/user";
import {useState} from "react";
import {Link} from "react-router-dom";

const Register = () => {
  const [inputEmail, setInputEmail] = useState('');
  const [inputPassword, setInputPassword] = useState('');
  const [inputUserName, setInputUserName] = useState('');

  const handleInputEmail = (e) => {
    setInputEmail(e.target.value);
  }

  const handleInputPassword = (e) => {
    setInputPassword(e.target.value);
  }

  const handleInputUserName = (e) => {
    setInputUserName(e.target.value);
  }

  const onClickRegisterBtn = () => {
    register(inputEmail, inputPassword, inputUserName);
  }
  return (
    <>
      <Col lg="6" md="8">
        <Card className="bg-secondary shadow border-0">
          <CardBody className="px-lg-5 py-lg-5">
            <div className="text-center text-muted mb-4">
              <small>회원가입</small>
            </div>
            <Form role="form">
              <FormGroup>
                <InputGroup className="input-group-alternative mb-3">
                  <InputGroupAddon addonType="prepend">
                    <InputGroupText>
                      <i className="ni ni-hat-3" />
                    </InputGroupText>
                  </InputGroupAddon>
                  <Input placeholder="사용할 이름" type="text"
                         value={inputUserName} onChange={handleInputUserName}/>
                </InputGroup>
              </FormGroup>
              <FormGroup>
                <InputGroup className="input-group-alternative mb-3">
                  <InputGroupAddon addonType="prepend">
                    <InputGroupText>
                      <i className="ni ni-email-83" />
                    </InputGroupText>
                  </InputGroupAddon>
                  <Input
                    placeholder="이메일"
                    type="email"
                    autoComplete="new-email"
                    value={inputEmail}
                    onChange={handleInputEmail}
                  />
                </InputGroup>
              </FormGroup>
              <FormGroup>
                <InputGroup className="input-group-alternative">
                  <InputGroupAddon addonType="prepend">
                    <InputGroupText>
                      <i className="ni ni-lock-circle-open" />
                    </InputGroupText>
                  </InputGroupAddon>
                  <Input
                    placeholder="비밀번호"
                    type="password"
                    autoComplete="new-password"
                    value={inputPassword}
                    onChange={handleInputPassword}
                  />
                </InputGroup>
              </FormGroup>
              <div className="text-center">
                <Button className="mt-4" color="primary" type="button" onClick={onClickRegisterBtn}>
                  회원 가입 하기
                </Button>
              </div>
            </Form>
          <br/>
            <div className="text-center">
              <Link to="/auth/login">
                <small>뒤로 가기</small>
              </Link>
            </div>
          </CardBody>
        </Card>
      </Col>
    </>
  );
};

export default Register;
