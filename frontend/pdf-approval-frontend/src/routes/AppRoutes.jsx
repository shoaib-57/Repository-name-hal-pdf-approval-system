import { Routes, Route } from "react-router-dom";
import Upload from "../pages/Upload/Upload";
import Documents from "../pages/Documents/Documents";
import Login from "../pages/Login/Login";
import Dashboard from "../pages/Dashboard/Dashboard";
import PublicDemo from "../pages/PublicDemo/PublicDemo";
import ProtectedRoute from "./ProtectedRoute";

function AppRoutes() {
  return (
    <Routes>
      <Route path="/" element={<PublicDemo />} />
      <Route path="/login" element={<Login />} />

      <Route element={<ProtectedRoute />}>
        <Route path="/dashboard" element={<Dashboard />} />
        <Route path="/upload" element={<Upload />} />
        <Route path="/documents" element={<Documents />} />
      </Route>
    </Routes>
  );
}

export default AppRoutes;
