import {
  FaHome,
  FaFileAlt,
  FaUpload,
  FaUsers,
  FaChartBar,
  FaQrcode,
  FaCog,
} from "react-icons/fa";
import "./Sidebar.css";
import { NavLink } from "react-router-dom";
import { FaSignOutAlt } from "react-icons/fa";
import { useNavigate } from "react-router-dom";
function Sidebar() {
  const navigate = useNavigate();

  const handleLogout = () => {
    localStorage.removeItem("token");

    localStorage.removeItem("user");

    navigate("/");
  };
  return (
    <aside className="sidebar">
      <div className="sidebar-logo">
        <h2>HAL Document Portal</h2>
      </div>

      <nav className="sidebar-nav">
        <ul className="sidebar-menu">
          <NavLink
            to="/dashboard"
            className={({ isActive }) =>
              isActive ? "sidebar-link active" : "sidebar-link"
            }
          >
            <li className="sidebar-item">
              <FaHome />
              <span>Dashboard</span>
            </li>
          </NavLink>
          <NavLink to="/documents" className="sidebar-link">
            <li className="sidebar-item">
              <FaFileAlt />
              <span>Documents</span>
            </li>
          </NavLink>

          <NavLink to="/upload" className="sidebar-link">
            <li className="sidebar-item">
              <FaUpload />
              <span>Upload Document</span>
            </li>
          </NavLink>
          <li className="sidebar-item">
            <FaQrcode />
            <span>Scan QR</span>
          </li>

          <div className="sidebar-footer">
            <button className="logout-btn" onClick={handleLogout}>
              <FaSignOutAlt />

              <span>Logout</span>
            </button>
          </div>
        </ul>
      </nav>
    </aside>
  );
}

export default Sidebar;
