// React imports.
import React from "react";


import Table from "@mui/material/Table";
import TableBody from "@mui/material/TableBody";
import TableCell from "@mui/material/TableCell";
import TableHead from "@mui/material/TableHead";
import TableRow from "@mui/material/TableRow";
import TableSortLabel from "@mui/material/TableSortLabel";


/**
 * MessageList.
 */
const MessageList = ({ state }): JSX.Element => {
  // Determine which messages to render based on sort direction
  const messagesToRender = state.sortBy === 'descending' 
    ? [...state.messages].reverse() 
    : state.messages;

  return (
    <Table stickyHeader padding="none">
      <TableHead>
        <TableRow>
          <TableCell style={{ width: 120 }}>
            <TableSortLabel
              active
              direction={state.sortBy === 'ascending' ? 'asc' : 'desc'}
              onClick={() => state.setSortBy(
                state.sortBy === 'ascending' ? 'descending' : 'ascending'
              )}
            >
              Date
            </TableSortLabel>
          </TableCell>
          <TableCell style={{ width: 300 }}>From</TableCell>
          <TableCell>Subject</TableCell>
        </TableRow>
      </TableHead>
      <TableBody>
        {messagesToRender.map(message => (
          <TableRow key={message.id} onClick={() => state.showMessage(message)}>
            <TableCell>{new Date(message.date).toLocaleDateString()}</TableCell>
            <TableCell>{message.from}</TableCell>
            <TableCell>{message.subject}</TableCell>
          </TableRow>
        ))}
      </TableBody>
    </Table>
  );
};


export default MessageList;
