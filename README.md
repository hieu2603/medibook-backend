
# Quy trình build docker của backend

## Bước 1: Xóa tất cả container, image, volume của dự án trong docker desktop (bước này chỉ làm 1 lần)

## Bước 2: git pull ở nhánh main để lấy source mới nhất
```bash
  git pull
```

## Bước 3: cd đến root /medibook-backend build và chạy backend (nếu làm từ bước 1 thì lâu ~3, 4 phút)
```bash
  docker-compose up --build -d
```
Khi tất cả các service đã run chờ khoảng 30s và vào http://localhost:8761/ để xem tình trạng service. Nếu thấy API-GATEWAY chưa UP thì khoan gọi request đợi API-GATEWAY khởi động (nó thường khởi dộng sau cùng) (F5 Refresh trang liên tục)

## Bước 4: ENJOY :D

## *Lưu ý: nếu phía backend có sửa code hoặc thêm tính năng gì mới thì chỉ cần làm lại từ bước 2 (nhưng trước khi docker-compose up --build -d thì xóa các container cũ đi). Không cần xóa image vì làm vậy sẽ quay về bước 1 rebuild lại image rất lâu :D