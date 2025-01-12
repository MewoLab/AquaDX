"""
This is a music recommendation system for maimai2 using implicit ALS.
"""
import json
from pathlib import Path

import pandas as pd
import requests
import scipy.sparse as sp
import implicit
from hypy_utils.logging_utils import setup_logger

BASE_URL = "https://aquadx.net/aqua/api/v2/game"
GAME = "mai2"
BOT_SECRET = "meow"

log = setup_logger()


if __name__ == '__main__':
    # Load the CSV data
    log.info("Loading data...")
    # data = pd.read_csv("data.csv")
    resp = requests.get(f"{BASE_URL}/{GAME}/recommender-fetch", params={"botSecret": BOT_SECRET})
    assert resp.status_code == 200, f"Failed to fetch data: {resp.status_code} {resp.text}"
    data = pd.read_csv(resp.text)

    # Create a user-item matrix
    log.info("Creating user-item matrix...")
    user_item_matrix = sp.csr_matrix((
        data['count'],
        (data['user_id'], data['music_id'])
    ))

    # Train an ALS model
    log.info("Training ALS model...")
    model = implicit.als.AlternatingLeastSquares(factors=50, regularization=0.01, iterations=15)
    model.fit(user_item_matrix)

    # Generate recommendations for each user
    log.info("Generating recommendations...")
    recommendations = {}
    for user_id in range(user_item_matrix.shape[0]):  # Loop over all users
        rec, prob = model.recommend(user_id, user_item_matrix[user_id], N=20)
        recommendations[user_id] = [int(item) for item in rec]

    # Save recommendations to a file
    log.info("Saving recommendations...")
    # Path("recommendations.json").write_text(json.dumps(recommendations))
    resp = requests.post(f"{BASE_URL}/{GAME}/recommender-update", params={"botSecret": BOT_SECRET}, json=recommendations)
