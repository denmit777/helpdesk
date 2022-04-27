import React from "react";
import PropTypes from "prop-types";
import {
  Paper,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
  Typography,
  Button,
} from "@material-ui/core";
import { FEEDBACKS_TABLE_COLUMNS } from "../constants/tablesColumns";

function FeedbacksTable(props) {
  const { feedbacks, showAllFeedbacksCallback } = props;

  return (
    <TableContainer component={Paper}>
      <div className="enter-comment-section__add-comment-button">
        <Button
          variant="contained"
          color="primary"
          onClick={(e) => showAllFeedbacksCallback(e)}
        >
          Show All
        </Button>
      </div>
      <Table>
        <TableHead>
          <TableRow>
            {FEEDBACKS_TABLE_COLUMNS.map((item) => {
              return (
                <TableCell key={item.id} align="center">
                  <Typography variant="h6">{item.label}</Typography>
                </TableCell>
              );
            })}
          </TableRow>
        </TableHead>
        <TableBody>
          {feedbacks.map((item, index) => {
            return (
              <TableRow key={index}>
                <TableCell align="center">{item.date}</TableCell>
                <TableCell align="center">{item.rate}</TableCell>
                <TableCell align="center">{item.text}</TableCell>
              </TableRow>
            );
          })}
        </TableBody>
      </Table>
    </TableContainer>
  );
}

FeedbacksTable.propTypes = {
  name: PropTypes.array,
};

export default FeedbacksTable;