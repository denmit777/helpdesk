import React from "react";
import { Link } from "react-router-dom";
import { withRouter } from "react-router";
import AttachmentService from '../services/AttachmentService';
import { Button } from "@material-ui/core";

class Attachment extends React.Component {
    constructor(props) {
        super(props)

        this.state = {
            attachment: [],
            file: null,
            fileName: ''
        }
    }

    componentDidMount() {
        const ticketId = this.props.match.params.id;
        const attachmentId = this.props.match.params.attachmentId;

        AttachmentService.getAttachmentById(attachmentId, ticketId).then((response) => {
            this.setState({ attachment: response.data });
        });
    }

    render() {
        return (
            <div>
                <div className={"buttons-container"}>
                    <Button variant="contained" color="secondary"
                        component={Link}
                        to={`/tickets/${this.props.match.params.id}`}
                    >
                        Ticket Overview
                    </Button>
                </div>
                <div className="ticket-data-container__info">
                    <img src={`data:image;base64,${this.state.attachment.file}`} width="100%" />
                </div>
            </div>
        )
    }
}

const AttachmentWithRouter = withRouter(Attachment);
export default AttachmentWithRouter;



