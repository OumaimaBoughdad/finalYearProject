import pandas as pd
import matplotlib.pyplot as plt
import seaborn as sns

# Load the CSV file
data = pd.read_csv("loan_requests.csv")

# Display the first few rows of the data
print(data.head())

# Ensure loan_status is treated as a categorical variable
data['loan_status'] = data['loan_status'].astype(int)

# 4. Bar Plot: Loan Status by Default on File
plt.figure(figsize=(8, 6))
sns.countplot(x='cb_person_default_on_file', hue='loan_status', data=data, palette='Set1')
plt.title('Loan Status by Default on File')
plt.xlabel('Default on File (Y/N)')
plt.ylabel('Count')
plt.legend(title='Loan Status', loc='upper right')
plt.show()

# 5. Bar Plot: Loan Status by Credit History Length (Binned)
# Bin the credit history length into categories for better visualization
data['cb_person_cred_hist_length_binned'] = pd.cut(data['cb_person_cred_hist_length'], bins=[0, 2, 5, 10, 20], labels=['0-2', '3-5', '6-10', '10+'])
plt.figure(figsize=(10, 6))
sns.countplot(x='cb_person_cred_hist_length_binned', hue='loan_status', data=data, palette='Set2')
plt.title('Loan Status by Credit History Length (Binned)')
plt.xlabel('Credit History Length (Years)')
plt.ylabel('Count')
plt.legend(title='Loan Status', loc='upper right')
plt.show()

