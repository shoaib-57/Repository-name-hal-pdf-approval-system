import "./DashboardSection.css";

function DashboardSection({
  title,
  actionText,
  children,
}) {
  return (
    <section className="dashboard-section">

      <div className="dashboard-section-header">

        <h2>{title}</h2>

        {actionText && (

          <button className="section-action">

            {actionText}

          </button>

        )}

      </div>

      <div className="dashboard-section-body">

        {children}

      </div>

    </section>
  );
}

export default DashboardSection;