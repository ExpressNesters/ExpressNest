import React, { useState } from 'react';
import "./CommentForm.css"

const CommentForm = ({ userId, postId, onCommentSubmitted }) => {
  const [commentText, setCommentText] = useState('');

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (!commentText) return;

    try {
      const response = await fetch(`http://a83ab0f0e6671462c87d9c3980002854-1490594495.us-west-2.elb.amazonaws.com/comments/`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ commentText, userId, postId }),
      });

      if (!response.ok) {
        throw new Error(`HTTP error! Status: ${response.status}`);
      }
      if(response.ok){
        setCommentText('');
        onCommentSubmitted(); // Notify parent component to refresh comments
      }

    } catch (error) {
      console.error('Failed to submit comment:', error);
    }
  };

  return (
    <form onSubmit={handleSubmit} className="comment-form">
      <textarea 
        value={commentText} 
        onChange={(e) => setCommentText(e.target.value)}
        placeholder="Write a comment..."
      />
      <button type="submit">Submit</button>
    </form>
  );
};

export default CommentForm;
