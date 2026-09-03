import "./StatsCard.css";

function StatsCard({ title, value, icon, color }) {
  return (
    <div className="stats-card">
      <div className="stats-content">
        <div>
          <p className="stats-title">{title}</p>

          <h2>{value}</h2>
        </div>

        <div className={`stats-icon ${color}`}>{icon}</div>
      </div>
    </div>
  );
}

export default StatsCard;