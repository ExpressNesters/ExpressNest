from flask import request, jsonify
from app import app
import app.models as models

@app.route('/follow', methods=['POST'])
def follow():
    data = request.json
    models.follow_user(data['follower_id'], data['followee_id'])
    return jsonify({'message': 'Followed successfully'}), 200

@app.route('/unfollow', methods=['POST'])
def unfollow():
    data = request.json
    models.unfollow_user(data['follower_id'], data['followee_id'])
    return jsonify({'message': 'Unfollowed successfully'}), 200

@app.route('/followers/<user_id>', methods=['GET'])
def followers(user_id):
    followers_data = models.get_followers(user_id)
    return jsonify(followers_data), 200