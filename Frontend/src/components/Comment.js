import React from 'react';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faTrash } from '@fortawesome/free-solid-svg-icons';
import "./Comment.css";

const Comment = ({ commentData, userId, onCommentDeleted }) => {
  const handleDeleteClick = async () => {
    try {
      const response = await fetch(`http://a83ab0f0e6671462c87d9c3980002854-1490594495.us-west-2.elb.amazonaws.com/comments/${commentData.commentId}`, {
        method: 'DELETE',
      });

      if (!response.ok) {
        throw new Error(`Error: ${response.status}`);
      }

      onCommentDeleted(); // Notify parent component to refresh comments
    } catch (error) {
      console.error('Failed to delete comment:', error);
    }
  };

  return (
    <div className="comment">
      <p>{commentData.commentText}</p>
      {/* Show delete button only if the current user created the comment */}
      {userId === commentData.userId && (
        <FontAwesomeIcon icon={faTrash} onClick={handleDeleteClick} className="comment-delete-icon" />
      )}
    </div>
  );
};

export default Comment;
