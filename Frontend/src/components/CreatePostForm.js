// src/components/CreatePostForm.js
import React, { useState } from 'react';
import './CreatePostForm.css';

const CreatePostForm = ({ onPostCreated }) => {
  const [postText, setPostText] = useState('');

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (!postText) return;

    // Replace with actual user ID as needed
    const postBody = {
      userId: 1, // Example user ID, replace with actual user ID
      postText: postText,
      privacy: "Public"
    };

    try {
      const response = await fetch("http://a83ab0f0e6671462c87d9c3980002854-1490594495.us-west-2.elb.amazonaws.com/posts/withoutAttachment", {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(postBody),
      });

      if (!response.ok) {
        throw new Error(`Error: ${response.status}`);
      }

      const data = await response.json();
      console.log(data)
      onPostCreated(); // Callback to inform parent component
      setPostText(''); // Reset the text field
    } catch (error) {
      console.error('Failed to create post:', error);
    }
  };

  return (
    <form onSubmit={handleSubmit}>
      <textarea
        value={postText}
        onChange={(e) => setPostText(e.target.value)}
        placeholder="What's on your mind?"
        rows="4"
        style={{ width: '100%' }}
      ></textarea>
      <button type="submit" style={{ marginTop: '10px' }}>Post</button>
    </form>
  );
};

export default CreatePostForm;
