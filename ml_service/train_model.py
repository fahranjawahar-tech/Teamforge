import numpy as np
import pandas as pd
from sklearn.ensemble import RandomForestRegressor
from sklearn.model_selection import train_test_split
from sklearn.metrics import mean_squared_error, r2_score
import joblib
import json
import os

def generate_synthetic_dataset(num_samples=1200, random_seed=42):
    """
    Generates synthetic historical college project/hackathon team data.
    Simulates realistic relationships between technical/soft skills,
    role diversity, collaboration, and final project success scores.
    """
    np.random.seed(random_seed)
    
    # 1. Individual and Team Skills (1.0 - 5.0 scale)
    avg_coding = np.random.uniform(1.8, 5.0, num_samples)
    avg_design = np.random.uniform(1.5, 5.0, num_samples)
    avg_comm = np.random.uniform(2.0, 5.0, num_samples)
    avg_doc = np.random.uniform(1.8, 5.0, num_samples)
    
    # 2. Diversity and Compatibility Factors (0.0 - 1.0 scale)
    role_diversity = np.random.uniform(0.3, 1.0, num_samples)
    interest_similarity = np.random.uniform(0.2, 1.0, num_samples)
    collaboration_score = np.random.uniform(0.4, 1.0, num_samples)
    
    # 3. Track Record
    team_experience = np.random.uniform(1.0, 5.0, num_samples) / 5.0 # normalized 0.2 - 1.0
    hackathon_win_rate = np.random.uniform(0.0, 0.85, num_samples)
    project_completion_rate = np.random.uniform(0.5, 1.0, num_samples)
    
    # Formulate ground-truth success score with non-linear synergies and weights
    # Weighted components (sums roughly to ~100 points maximum)
    skill_component = (
        (avg_coding / 5.0) * 26.0 +
        (avg_design / 5.0) * 16.0 +
        (avg_comm / 5.0) * 18.0 +
        (avg_doc / 5.0) * 12.0
    ) # max 72 pts
    
    synergy_component = (
        role_diversity * 12.0 +
        interest_similarity * 6.0 +
        collaboration_score * 6.0
    ) # max 24 pts
    
    experience_component = (
        team_experience * 6.0 +
        hackathon_win_rate * 6.0 +
        project_completion_rate * 6.0
    ) # max 18 pts
    
    # Non-linear penalty if coding or communication is severely lacking
    bottleneck_penalty = np.where(avg_coding < 2.5, -10.0, 0.0) + np.where(avg_comm < 2.2, -8.0, 0.0)
    # Role diversity bonus: high role diversity enhances skill component
    diversity_bonus = np.where(role_diversity > 0.8, 4.0, 0.0)
    
    raw_score = skill_component + synergy_component + experience_component + bottleneck_penalty + diversity_bonus
    
    # Normalize with realistic noise
    noise = np.random.normal(0, 2.5, num_samples)
    success_score = np.clip(raw_score * 0.88 + noise, 35.0, 98.5)
    
    df = pd.DataFrame({
        'average_coding': avg_coding,
        'average_design': avg_design,
        'average_communication': avg_comm,
        'average_documentation': avg_doc,
        'role_diversity': role_diversity,
        'interest_similarity': interest_similarity,
        'collaboration_score': collaboration_score,
        'team_experience': team_experience,
        'hackathon_win_rate': hackathon_win_rate,
        'project_completion_rate': project_completion_rate,
        'success_score': success_score
    })
    
    return df

def train_and_save_model():
    print("[1/4] Generating synthetic college project dataset...")
    df = generate_synthetic_dataset(num_samples=1500)
    
    feature_cols = [
        'average_coding',
        'average_design',
        'average_communication',
        'average_documentation',
        'role_diversity',
        'interest_similarity',
        'collaboration_score',
        'team_experience',
        'hackathon_win_rate',
        'project_completion_rate'
    ]
    
    X = df[feature_cols]
    y = df['success_score']
    
    X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=0.2, random_state=42)
    
    print("[2/4] Training Random Forest Regressor model...")
    model = RandomForestRegressor(
        n_estimators=120,
        max_depth=10,
        min_samples_split=4,
        min_samples_leaf=2,
        random_state=42
    )
    model.fit(X_train, y_train)
    
    print("[3/4] Evaluating Model Performance...")
    y_pred = model.predict(X_test)
    mse = mean_squared_error(y_test, y_pred)
    rmse = np.sqrt(mse)
    r2 = r2_score(y_test, y_pred)
    
    print(f" -> Test RMSE: {rmse:.3f}")
    print(f" -> Test R^2 Score: {r2:.3f}")
    
    importances = dict(zip(feature_cols, [round(float(v), 4) for v in model.feature_importances_]))
    print(" -> Feature Importances:", importances)
    
    print("[4/4] Saving model artifacts...")
    current_dir = os.path.dirname(os.path.abspath(__file__))
    model_path = os.path.join(current_dir, 'team_success_model.joblib')
    joblib.dump(model, model_path)
    
    metadata = {
        'model_type': 'RandomForestRegressor',
        'features': feature_cols,
        'metrics': {
            'rmse': round(float(rmse), 3),
            'r2': round(float(r2), 3)
        },
        'feature_importances': importances,
        'description': 'Trained on 1500 synthetic historical college hackathon/project team records'
    }
    
    metadata_path = os.path.join(current_dir, 'model_metadata.json')
    with open(metadata_path, 'w') as f:
        json.dump(metadata, f, indent=2)
        
    print(f"Model successfully saved to {model_path}")
    print(f"Metadata saved to {metadata_path}")

if __name__ == '__main__':
    train_and_save_model()
