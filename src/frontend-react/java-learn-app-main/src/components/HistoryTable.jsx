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
import { HISTORY_TABLE_COLUMNS } from "../constants/tablesColumns";

function HistoryTable(props) {
  const { history, showAllHistoryCallback } = props;

  return (
    <TableContainer component={Paper}>
      <div className="enter-comment-section__add-comment-button">
        <Button
          variant="contained"
          color="primary"
          onClick={(e) => showAllHistoryCallback(e)}
        >
          Show All
        </Button>
      </div>
      <Table>
        <TableHead>
          <TableRow>
            {HISTORY_TABLE_COLUMNS.map((item) => {
              return (
                <TableCell key={item.id} align="center">
                  <Typography variant="h6">{item.label}</Typography>
                </TableCell>
              );
            })}
          </TableRow>
        </TableHead>
        <TableBody>
          {history.map((item, index) => {
            return (
              <TableRow key={index}>
                <TableCell align="center">{item.date}</TableCell>
                <TableCell align="center">{item.user}</TableCell>
                <TableCell align="center">{item.action}</TableCell>
                <TableCell align="left">{item.description}</TableCell>
              </TableRow>
            );
          })}
        </TableBody>
      </Table>
    </TableContainer>
  );
}

HistoryTable.propTypes = {
  name: PropTypes.array,
};

export default HistoryTable;
