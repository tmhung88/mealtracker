import { withRouter } from "react-router-dom";

export function withPage(ComponentToProtect) {
    return withRouter(ComponentToProtect);    
}