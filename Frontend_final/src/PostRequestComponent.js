import React, { useState } from 'react';

const PostRequestComponent = () => {
    const [response, setResponse] = useState(null);

    const makePostRequest = async () => {
        try {
            const response = await fetch('http://a83ab0f0e6671462c87d9c3980002854-1490594495.us-west-2.elb.amazonaws.com/posts/withoutAttachment', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({
                    "userId": 1,
                    "postText": "We are about to end our First Semester at SJSU",
                    "privacy": "Public"
                }),
            });

            const data = await response.json();
            setResponse(data);
        } catch (error) {
            console.error('Error making the POST request:', error);
        }
    };

    return (
        <div>
            <button onClick={makePostRequest}>Send POST Request</button>
            {response && <div>Response: {JSON.stringify(response)}</div>}
        </div>
    );
};

export default PostRequestComponent;
