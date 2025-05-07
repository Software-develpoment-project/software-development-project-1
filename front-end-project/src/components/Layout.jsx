import React from 'react';
import { Link } from 'react-router-dom';
import { Container, Navbar, Nav } from 'react-bootstrap';

const Layout = ({ children }) => {
  return (
    <>
      <Navbar bg="light" expand="lg" className="mb-3 shadow-sm">
        <Container>
          <Navbar.Brand as={Link} to="/">Quiz App</Navbar.Brand>
          <Navbar.Toggle aria-controls="basic-navbar-nav" />
          <Navbar.Collapse id="basic-navbar-nav">
            <Nav className="me-auto">
              <Nav.Link as={Link} to="/quizzes">
                My Quizzes
              </Nav.Link>
              <Nav.Link as={Link} to="/published-quizzes">
                Published Quizzes
              </Nav.Link>
              <Nav.Link as={Link} to="/all-quizzes">
                All Quizzes
              </Nav.Link>
            </Nav>
          </Navbar.Collapse>
        </Container>
      </Navbar>

      <main>
        <Container className="py-4">
          {children}
        </Container>
      </main>
    </>
  );
};

export default Layout; 