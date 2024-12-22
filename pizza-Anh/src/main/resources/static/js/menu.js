// Đối tượng lưu trữ các giá trị size, crust, và quantity
const selectedOptions = {
  size: null,
  crust: null,
  quantity: 1,
  productId: document.getElementById("productId").value,
};

// Hàm cập nhật các tùy chọn crust theo size
function updateCrustOptions(selectedRadio) {
  const selectedSize = selectedRadio.getAttribute("data-size");
  selectedOptions.size = selectedSize; // Cập nhật size vào đối tượng lưu trữ
  console.log("Selected Size:", selectedSize);

  const crustOptionsDiv = document.getElementById("crustOptions");
  crustOptionsDiv.innerHTML = ""; // Xóa các crust cũ

  // Gửi yêu cầu để lấy danh sách crust
  fetch(`/menu/getCrustOptions?size=${selectedSize}`)
    .then((response) => response.json())
    .then((crustOptions) => {
      crustOptions.forEach((crustOption) => {
        const radioButton = document.createElement("input");
        radioButton.type = "radio";
        radioButton.name = "crustId";
        radioButton.value = crustOption.crust;
        radioButton.onclick = () => updateSelectedOptions(radioButton);

        const label = document.createElement("label");
        label.textContent = `${crustOption.crust} - ${crustOption.additionalPrice} VND`;

        const div = document.createElement("div");
        div.appendChild(radioButton);
        div.appendChild(label);
        crustOptionsDiv.appendChild(div);
      });
    })
    .catch((error) => console.error("Error fetching crust options:", error));
}

// Hàm cập nhật giá trị size, crust, và quantity
function updateSelectedOptions(inputElement) {
  const name = inputElement.name;
  const value = inputElement.value;

  if (name === "sizeId") {
    selectedOptions.size = value;
  } else if (name === "crustId") {
    selectedOptions.crust = value;
  } else if (inputElement.id === "quantity") {
    selectedOptions.quantity = inputElement.value;
  }

  console.log("Updated Options:", selectedOptions);
  calculateAndUpdatePrice();
}

// Hàm tính giá dựa trên các tùy chọn
function calculateAndUpdatePrice() {
  const { size, crust, quantity, productId } = selectedOptions;

  if (!size || !crust) {
    alert("Please select both size and crust!");
    return;
  }

  fetch(
    `/menu/calculatePrice?size=${size}&crust=${crust}&quantity=${quantity}&productId=${productId}`
  )
    .then((response) => response.json())
    .then((data) => {
      if (data.price) {
        document.getElementById("price").textContent = data.price + " VND";
      } else {
        document.getElementById("price").textContent = "Error occurred";
      }
    })
    .catch((error) => console.error("Error calculating price:", error));
}

// Lắng nghe sự kiện thay đổi của quantity
document.getElementById("quantity").addEventListener("change", (event) => {
  selectedOptions.quantity = event.target.value;
  calculateAndUpdatePrice();
});
