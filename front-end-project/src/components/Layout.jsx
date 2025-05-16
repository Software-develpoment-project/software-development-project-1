import React, { useState } from 'react';
import { Link as RouterLink, useLocation } from 'react-router-dom';
import {
  AppBar,
  Toolbar,
  Typography,
  Button,
  Container,
  Box,
  IconButton,
  Drawer,
  List,
  ListItem,
  ListItemButton,
  ListItemText,
  Divider,
  ListItemIcon,
  Tooltip
} from '@mui/material';
import MenuIcon from '@mui/icons-material/Menu';
import SchoolIcon from '@mui/icons-material/School';
import AdminPanelSettingsIcon from '@mui/icons-material/AdminPanelSettings';
import ListAltIcon from '@mui/icons-material/ListAlt';
import CategoryIcon from '@mui/icons-material/Category';
import DashboardIcon from '@mui/icons-material/Dashboard';

const teacherNavItems = [
  { label: 'My Quizzes', path: '/quizzes', icon: <ListAltIcon /> },
  { label: 'Manage Categories', path: '/categories', icon: <CategoryIcon /> },
];

const studentNavItems = [
  { label: 'Available Quizzes', path: '/student/quizzes', icon: <ListAltIcon /> },
  { label: 'Browse Categories', path: '/student/categories', icon: <CategoryIcon /> },
];

const Layout = ({ children }) => {
  const [mobileOpen, setMobileOpen] = useState(false);
  const location = useLocation();

  const handleDrawerToggle = () => {
    setMobileOpen((prevState) => !prevState);
  };

  const isStudentPathActive = location.pathname.startsWith('/student');
  const isTeacherPathActive = !isStudentPathActive;

  const drawerWidth = 280;

  const drawerContent = (
    <Box onClick={handleDrawerToggle} sx={{ textAlign: 'center' }}>
      <Typography variant="h6" sx={{ my: 2, color: 'primary.main', fontWeight: 'bold' }}>
        Quiz App Menu
      </Typography>
      <Divider />
      <List subheader={<Typography variant="overline" sx={{ pl: 2, display: 'block', color: 'text.secondary' }}>Teacher Area</Typography>}>
        <ListItem disablePadding>
          <ListItemButton component={RouterLink} to="/quizzes" selected={isTeacherPathActive && teacherNavItems.some(item => location.pathname.startsWith(item.path.split('/')[1]))}>
            <ListItemIcon>
              <AdminPanelSettingsIcon color={isTeacherPathActive ? "primary" : "action"} />
            </ListItemIcon>
            <ListItemText primary="Teacher Dashboard" primaryTypographyProps={{ fontWeight: isTeacherPathActive ? 'bold' : 'normal' }} />
          </ListItemButton>
        </ListItem>
        {teacherNavItems.map((item) => (
          <ListItem key={`drawer-teacher-${item.label}`} disablePadding sx={{ pl: 2 }}>
            <ListItemButton component={RouterLink} to={item.path} selected={location.pathname === item.path}>
              <ListItemIcon>{React.cloneElement(item.icon, { color: location.pathname === item.path ? "primary" : "action" })}</ListItemIcon>
              <ListItemText primary={item.label} />
            </ListItemButton>
          </ListItem>
        ))}
      </List>
      <Divider />
      <List subheader={<Typography variant="overline" sx={{ pl: 2, display: 'block', color: 'text.secondary' }}>Student Area</Typography>}>
        <ListItem disablePadding>
          <ListItemButton component={RouterLink} to="/student/quizzes" selected={isStudentPathActive && studentNavItems.some(item => location.pathname.startsWith(item.path.split('/')[2]))}>
            <ListItemIcon>
              <SchoolIcon color={isStudentPathActive ? "primary" : "action"} />
            </ListItemIcon>
            <ListItemText primary="Student Dashboard" primaryTypographyProps={{ fontWeight: isStudentPathActive ? 'bold' : 'normal' }} />
          </ListItemButton>
        </ListItem>
        {studentNavItems.map((item) => (
          <ListItem key={`drawer-student-${item.label}`} disablePadding sx={{ pl: 2 }}>
            <ListItemButton component={RouterLink} to={item.path} selected={location.pathname === item.path}>
              <ListItemIcon>{React.cloneElement(item.icon, { color: location.pathname === item.path ? "primary" : "action" })}</ListItemIcon>
              <ListItemText primary={item.label} />
            </ListItemButton>
          </ListItem>
        ))}
      </List>
    </Box>
  );

  return (
    <Box sx={{ display: 'flex', flexDirection: 'column', minHeight: '100vh' }}>
      <AppBar component="nav" position="static" sx={{ mb: 0, boxShadow: 3 }}>
        <Container maxWidth="lg">
          <Toolbar disableGutters>
            <IconButton
              color="inherit"
              aria-label="open drawer"
              edge="start"
              onClick={handleDrawerToggle}
              sx={{ mr: 2, display: { md: 'none' } }}
            >
              <MenuIcon />
            </IconButton>

            <DashboardIcon sx={{ mr: 1, display: { xs: 'none', md: 'flex' } }} />
            <Typography
              variant="h6"
              component={RouterLink}
              to={isStudentPathActive ? "/student/quizzes" : "/quizzes"}
              sx={{
                display: { xs: 'none', md: 'flex' },
                fontFamily: 'monospace',
                fontWeight: 700,
                letterSpacing: '.1rem',
                color: 'inherit',
                textDecoration: 'none',
                mr: 2
              }}
            >
              QUIZ APP
            </Typography>

             <Typography
              variant="h6"
              component="div"
              sx={{
                flexGrow: 1,
                display: { xs: 'flex', md: 'none' },
                justifyContent: 'center',
                color: 'inherit',
                textDecoration: 'none',
              }}
            >
             {isStudentPathActive ? "Student" : "Teacher"}
            </Typography>

            <Box sx={{ flexGrow: 1, display: { xs: 'none', md: 'flex' } }}>
              {isTeacherPathActive && teacherNavItems.map((item) => (
                <Button
                  key={`appbar-teacher-${item.label}`}
                  component={RouterLink}
                  to={item.path}
                  startIcon={React.cloneElement(item.icon, {color: 'inherit'})}
                  sx={{ my: 2, color: 'white', display: 'block',
                        fontWeight: location.pathname.startsWith(item.path) ? 'bold' : 'normal',
                        borderBottom: location.pathname.startsWith(item.path) ? '2px solid white' : 'none'
                  }}
                >
                  {item.label}
                </Button>
              ))}
              {isStudentPathActive && studentNavItems.map((item) => (
                 <Button
                  key={`appbar-student-${item.label}`}
                  component={RouterLink}
                  to={item.path}
                  startIcon={React.cloneElement(item.icon, {color: 'inherit'})}
                  sx={{ my: 2, color: 'white', display: 'block',
                        fontWeight: location.pathname.startsWith(item.path) ? 'bold' : 'normal',
                        borderBottom: location.pathname.startsWith(item.path) ? '2px solid white' : 'none',
                        mx: 0.5
                  }}
                >
                  {item.label}
                </Button>
              ))}
            </Box>

            <Box sx={{ display: { xs: 'none', md: 'flex' } }}>
              <Tooltip title="Switch to Teacher Dashboard">
                <IconButton component={RouterLink} to="/quizzes" color="inherit"
                    sx={{bgcolor: isTeacherPathActive ? 'rgba(255,255,255,0.2)' : 'transparent'}}>
                  <AdminPanelSettingsIcon />
                </IconButton>
              </Tooltip>
              <Tooltip title="Switch to Student Dashboard">
                <IconButton component={RouterLink} to="/student/quizzes" color="inherit"
                    sx={{bgcolor: isStudentPathActive ? 'rgba(255,255,255,0.2)' : 'transparent'}}>
                  <SchoolIcon />
                </IconButton>
              </Tooltip>
            </Box>

          </Toolbar>
        </Container>
      </AppBar>

      <Drawer
        variant="temporary"
        open={mobileOpen}
        onClose={handleDrawerToggle}
        ModalProps={{
          keepMounted: true,
        }}
        sx={{
          display: { xs: 'block', md: 'none' },
          '& .MuiDrawer-paper': { boxSizing: 'border-box', width: drawerWidth },
        }}
      >
        {drawerContent}
      </Drawer>

      <Box component="main" sx={{ flexGrow: 1, bgcolor: 'grey.100', py: 3, px: { xs: 1, sm: 2, md: 3 } }}>
        {children}
      </Box>

      <Box component="footer" sx={{ bgcolor: 'primary.dark', color: 'white', p: 2, textAlign: 'center', mt: 'auto' }}>
        <Typography variant="body2">
          © {new Date().getFullYear()} Quiz Application. All rights reserved.
        </Typography>
      </Box>
    </Box>
  );
};

export default Layout;