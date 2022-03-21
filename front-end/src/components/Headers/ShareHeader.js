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
import {Card, CardBody, CardTitle, Container, Row, Col, Button} from "reactstrap";
import { CgDatabase } from "react-icons/cg";
import {useEffect, useState} from "react";
import client from "../../api/client";

const ShareHeader = () => {
  const [user, setUser] = useState(null);

  useEffect(() => {
        client.get("http://192.168.0.26:8080/user/myInfo")
            .then(response => {
              setUser(response.data);
            }).catch(e => {
          console.error(e);
        })
      }, []
  )

  return (
    <>
      <div className="header bg-gradient-info pb-8 pt-5 pt-md-8">
        <span className="mask bg-gradient-default opacity-8" />
        <Container fluid>
          <Row>
            <Col lg="7" md="10">
              <h1 className="display-2 text-white">File Share</h1>
              <p className="text-white mt-0 mb-5">
                서비스에 가입된 사용자 간의 파일 공유를 할 수 있습니다.
                공유된 각 파일은 5일이 지나면 자동적으로 삭제됩니다.
              </p>
            </Col>
          </Row>
          <div className="header-body">
            {/* Card stats */}
            <Row>
              <Col lg="6" xl="3">
                <Card className="card-stats mb-4 mb-xl-0">
                  <CardBody>
                    <Row>
                      <div className="col">
                        <CardTitle
                          tag="h5"
                          className="text-uppercase text-muted mb-0"
                        >
                          사용 정보
                        </CardTitle>
                        <span className="h2 font-weight-bold mb-0">
                          {user?.currentSize} bytes
                        </span>
                      </div>
                      <Col className="col-auto">
                        <div className="icon icon-shape bg-gradient-blue text-white rounded-circle shadow">
                          <CgDatabase />
                        </div>
                      </Col>
                    </Row>
                  </CardBody>
                </Card>
              </Col>
            </Row>
          </div>
        </Container>
      </div>
    </>
  );
};

export default ShareHeader;
