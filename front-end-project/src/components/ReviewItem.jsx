import React from 'react';
import {
  Paper, Typography, Rating, Box, Stack, IconButton, Divider
} from '@mui/material';
import EditIcon from '@mui/icons-material/Edit';
import DeleteIcon from '@mui/icons-material/Delete';
import AccountCircleIcon from '@mui/icons-material/AccountCircle';

const ReviewItem = ({ review, currentStudentNickname, onEdit, onDelete }) => {
  const canModify = review.studentNickname && currentStudentNickname && review.studentNickname.toLowerCase() === currentStudentNickname.toLowerCase();

  return (
    <Paper elevation={2} sx={{ p: 2.5, mb: 2.5, borderRadius: 2 }}>
      <Stack spacing={1.5}>
        <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
          <Box sx={{display: 'flex', alignItems: 'center'}}>
            <AccountCircleIcon sx={{mr: 1, color: 'text.secondary'}}/>
            <Typography variant="h6" component="span" sx={{ fontWeight: 'medium' }}>
                {review.studentNickname || 'Anonymous'}
            </Typography>
          </Box>
          {canModify && (
            <Box>
              <IconButton size="small" onClick={() => onEdit(review)} color="primary" aria-label="edit review">
                <EditIcon fontSize="small"/>
              </IconButton>
              <IconButton size="small" onClick={() => onDelete(review.id)} color="error" aria-label="delete review">
                <DeleteIcon fontSize="small"/>
              </IconButton>
            </Box>
          )}
        </Box>
        <Rating value={review.rating || 0} readOnly precision={0.5} />
        <Typography variant="body1" sx={{ whiteSpace: 'pre-line', color: 'text.secondary', fontStyle: 'italic' }}>
          "{review.reviewText || 'No text provided.'}"
        </Typography>
        <Divider sx={{pt:1}}/>
        <Typography variant="caption" color="text.disabled" sx={{textAlign: 'right'}}>
          Written on: {review.createdAt ? new Date(review.createdAt).toLocaleDateString() : 'N/A'}
        </Typography>
      </Stack>
    </Paper>
  );
};

export default ReviewItem;