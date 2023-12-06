import React, { useState } from 'react';
import './Post.css'; // Make sure you have your CSS file for styling

const Post = ({ postData, onPostUpdated }) => {
  const [isEditing, setIsEditing] = useState(false);
  const [editText, setEditText] = useState(postData.postText);

  const handleEditClick = () => {
    setIsEditing(true);
  };


  const handleSaveClick = async () => {
    const updatedPost = {
      userId: postData.userId,
      postId: postData.postId,
      postText: editText,
      privacy: "Public"
    };

    try {
      const response = await fetch(`http://a83ab0f0e6671462c87d9c3980002854-1490594495.us-west-2.elb.amazonaws.com/posts/${postData.postId}`, {
        method: 'PUT',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(updatedPost),
      });

      if (!response.ok) {
        throw new Error(`Error: ${response.status}`);
      }

      setIsEditing(false);
      onPostUpdated(); // Notify parent component to refresh posts
    } catch (error) {
      console.error('Failed to update post:', error);
    }
  };

  // Inside your Post component
  const handleDeleteClick = async () => {
    try {
        console.log(`http://a83ab0f0e6671462c87d9c3980002854-1490594495.us-west-2.elb.amazonaws.com/posts/${postData.postId}`)
      const response = await fetch(`http://a83ab0f0e6671462c87d9c3980002854-1490594495.us-west-2.elb.amazonaws.com/posts/${postData.postId}`, {
        method: 'DELETE'
      });
      console.log(response)
  
      if (!response.ok) {
        throw new Error(`Error: ${response.status}`);
      }
  
      onPostUpdated(); // Notify parent component to refresh posts after deletion
    } catch (error) {
      console.error('Failed to delete post:', error);
    }
  };

  if (postData.deletedAt !== null) {
    return null;
  }

return (
    <div className="post">
      {isEditing ? (
        <>
          <textarea 
            value={editText} 
            onChange={(e) => setEditText(e.target.value)} 
          />
          <button onClick={handleSaveClick}>Save</button>
          <button onClick={() => setIsEditing(false)}>Cancel</button>
        </>
      ) : (
        <>
          <p>{postData.postText}</p>
          <button onClick={handleEditClick}>Edit</button>
          <button onClick={handleDeleteClick}>Delete</button>
        </>
      )}
    </div>
  );
  
};

export default Post;
