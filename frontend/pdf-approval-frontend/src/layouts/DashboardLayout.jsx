import "./DashboardLayout.css";

import Sidebar from "../components/layout/Sidebar/Sidebar";
import TopNavbar from "../components/layout/TopNavbar/TopNavbar";

function DashboardLayout({ children }) {

    return (

        <div className="dashboard-layout">

            <Sidebar />

            <div className="dashboard-main">

                <TopNavbar />

                <main className="dashboard-content">

                    {children}

                </main>

            </div>

        </div>

    );

}

export default DashboardLayout;