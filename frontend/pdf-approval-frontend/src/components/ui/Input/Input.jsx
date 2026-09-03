// import "./Input.css";

// function Input({
//     label,
//     type = "text",
//     name,
//     placeholder,
//     value,
//     onChange,
//     required = false
// }) {

//     return (

//         <div className="input-group">

//             <label className="input-label">

//                 {label}

//                 {required && <span className="required">*</span>}

//             </label>

//             <input
//                 className="input-field"
//                 type={type}
//                 name={name}
//                 placeholder={placeholder}
//                 value={value}
//                 onChange={onChange}
//             />

//         </div>

//     );

// }

// export default Input;
import "./Input.css";
import { useState } from "react";
import { FaEye, FaEyeSlash } from "react-icons/fa";

function Input({
    label,
    type = "text",
    name,
    placeholder,
    value,
    onChange,
    required = false
}) {

    const [showPassword, setShowPassword] = useState(false);

    const inputType =
        type === "password"
            ? (showPassword ? "text" : "password")
            : type;

    return (

        <div className="input-group">

            <label className="input-label">

                {label}

                {required && <span className="required">*</span>}

            </label>

            <div className="input-wrapper">

                <input
                    className="input-field"
                    type={inputType}
                    name={name}
                    placeholder={placeholder}
                    value={value}
                    onChange={onChange}
                />

                {type === "password" && (

                    <button
                        type="button"
                        className="password-toggle"
                        onClick={() =>
                            setShowPassword(!showPassword)
                        }
                    >

                        {showPassword
                            ? <FaEyeSlash />
                            : <FaEye />}

                    </button>

                )}

            </div>

        </div>

    );

}

export default Input;