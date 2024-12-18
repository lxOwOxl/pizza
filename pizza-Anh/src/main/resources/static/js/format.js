// Hàm định dạng số với dấu phân cách hàng nghìn
function formatNumber(input) {
  let value = input.value.replace(/\D/g, ""); // Loại bỏ tất cả ký tự không phải là số
  if (value.length > 0) {
    // Định dạng lại số với dấu phân cách hàng nghìn
    value = Number(value).toLocaleString(); // Định dạng giá trị thành chuỗi với dấu phân cách
  }
  input.value = value; // Cập nhật lại giá trị của ô input
}

// Hàm này giúp lấy giá trị mà không có định dạng, để lưu vào DB
function getNumberWithoutFormat(input) {
  let value = parseInt(input.value.replace(/\D/g, ""), 10);
  input.value = value;
}
