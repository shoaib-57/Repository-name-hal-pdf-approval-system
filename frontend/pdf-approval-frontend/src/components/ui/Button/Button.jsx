import "./Button.css";

function Button({
    type = "button",
    text,
    onClick,
    disabled = false
}) {

    return (

        <button
            type={type}
            className="btn"
            onClick={onClick}
            disabled={disabled}
        >
            {text}
        </button>

    );

}

export default Button;