import React, { useState, useEffect } from 'react';
import Comment from './Comment';
import CommentForm from './CommentForm';  // Assuming CommentForm is in the same directory

const CommentsList = ({ postId, userId }) => {
  const [allComments, setAllComments] = useState([]);
  const [currentComments, setCurrentComments] = useState([]);
  const [currentPage, setCurrentPage] = useState(0);
  const [totalPages, setTotalPages] = useState(0);
  const pageSize = 10; // Adjust based on your page size

  useEffect(() => {
    fetchAllComments();
  }, []);

  const fetchAllComments = async () => {
    let page = 0;
    let hasMore = true;
    let accumulatedComments = [];

    while (hasMore) {
      try {
        const response = await fetch(`http://a83ab0f0e6671462c87d9c3980002854-1490594495.us-west-2.elb.amazonaws.com/comments/post/${postId}?page=${page}&size=10&sort=createdAt,desc`);
        if (!response.ok) {
          throw new Error(`HTTP error! Status: ${response.status}`);
        }
        const data = await response.json();
        accumulatedComments = accumulatedComments.concat(data.commentDTOs.filter(comment => comment.deletedAt === null));

        setTotalPages(Math.ceil(accumulatedComments.length / pageSize));
        if (data.commentDTOs.length < 10 || page === data.totalPages - 1) {
          hasMore = false;
        } else {
          page++;
        }
      } catch (error) {
        console.error('Failed to fetch comments:', error);
        hasMore = false;
      }
    }

    setAllComments(accumulatedComments);
    updateCurrentComments(0, accumulatedComments);
  };

  const updateCurrentComments = (page, comments) => {
    const indexOfLastComment = (page + 1) * pageSize;
    const indexOfFirstComment = indexOfLastComment - pageSize;
    setCurrentComments(comments.slice(indexOfFirstComment, indexOfLastComment));
  };

  const handlePageChange = (page) => {
    setCurrentPage(page);
    updateCurrentComments(page, allComments);
  };

  const handleCommentSubmitted = () => {
    fetchAllComments(); // Re-fetch all comments
  };

  return (
    <div>
      <CommentForm
        userId={userId}
        postId={postId}
        onCommentSubmitted={handleCommentSubmitted}
      />
      {currentComments.map(comment => (
        <Comment 
          key={comment.commentId} 
          commentData={comment} 
          userId={userId} 
          onCommentDeleted={handleCommentSubmitted}
        />
      ))}
      <div className="pagination">
        {Array.from({ length: totalPages }, (_, index) => (
          <button 
            key={index} 
            onClick={() => handlePageChange(index)} 
            className={currentPage === index ? 'active' : ''}
          >
            {index + 1}
          </button>
        ))}
      </div>
    </div>
  );
};

export default CommentsList;
