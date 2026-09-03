import "./DashboardHeader.css";

function DashboardHeader({ title, subtitle }) {
  const user = JSON.parse(localStorage.getItem("user"));

  return (
    <div className="dashboard-header">
      <div>
        <h1>{title}</h1>

        <p>Welcome, {user?.firstName}! Manage your documents efficiently.</p>
      </div>
    </div>
  );
}

export default DashboardHeader;
