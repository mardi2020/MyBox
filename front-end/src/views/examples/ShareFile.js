import Header from "../../components/Headers/ShareHeader";
import React from "react";
// reactstrap components
import {
    Button,
    Card,
    CardBody,
    CardTitle,
    CardText,
    Container,
    Row
} from "reactstrap";


const ShareFile = () => {
    return (
        <>
            <Header />
            <Container className="mt--7" fluid>
            <Card style={{ width: "18rem" }}>
                <CardBody>
                    <CardTitle>어쩌고 저쩌고 님 전송</CardTitle>
                    <CardText>
                        이것은 파일에 대한 간략한 설명입니다.
                    </CardText>
                    <Row className="justify-content-center">
                    <Button
                        color="success"
                        href="#pablo"
                        onClick={e => e.preventDefault()}
                    >
                        다운로드
                    </Button>
                    <Button
                        color="danger"
                        href="#pablo"
                        onClick={e => e.preventDefault()}
                    >
                        삭제하기
                    </Button>
                    </Row>
                </CardBody>
            </Card>
            </Container>
        </>
    );
};

export default ShareFile;