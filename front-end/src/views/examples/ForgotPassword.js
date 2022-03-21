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
import {findPassword} from "../../api/user";
import {useState} from "react";
import { Link } from 'react-router-dom';

const ForgotPassword = () => {
  const [inputEmail, setInputEmail] = useState('');


  const handleInputId = (e) => {
    setInputEmail(e.target.value);
  }

  const onClickFindPwBtn = () => {
    findPassword(inputEmail)
  }

  return (
    <>
      <Col lg="5" md="7">
        <Card className="bg-secondary shadow border-0">
          <CardBody className="px-lg-5 py-lg-5">
            <div className="text-center text-muted mb-4">
              <small>비밀번호 찾기</small>
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
              <div className="text-center">
                <Button className="my-4" color="primary" type="button" onClick={onClickFindPwBtn}>비밀번호 전송</Button>
              </div>
            </Form>
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

export default ForgotPassword;
