import "./TopNavbar.css";
import { FaBell, FaChevronDown } from "react-icons/fa";

function TopNavbar() {

    const user = JSON.parse(localStorage.getItem("user"));

    return (

        <header className="top-navbar">

            <div className="navbar-title">

                HAL Document Portal

            </div>

            <div className="navbar-right">

                <button className="notification-btn">

                    <FaBell />

                </button>

                <div className="user-info">

                    <div className="avatar">

                        {user?.firstName?.charAt(0)}

                    </div>

                    <div className="user-details">

                        <span className="user-name">

                            {user?.firstName} {user?.lastName}

                        </span>

                        <span className="user-role">

                            {user?.role}

                        </span>

                    </div>

                    <FaChevronDown className="dropdown-icon" />

                </div>

            </div>

        </header>

    );

}

export default TopNavbar;