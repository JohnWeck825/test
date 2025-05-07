package com.myshop.adminpage.constant;

public enum OrderStatus {

    PENDING, AWAITING_PAYMENT, PAYMENT_REVIEW, PROCESSING, PACKAGING, DELIVERING, IN_TRANSIT, OUT_FOR_DELIVERY, DELIVERED, COMPLETED, CANCELLED, REFUNDED, FAILED
    //    Chờ xử lý
//    PENDING, // (Đang chờ xử lý) - Đây là trạng thái mặc định khi đơn hàng được tạo, cho biết đơn hàng đã được hệ thống ghi nhận nhưng chưa được xử lý bởi nhân viên.
//    AWAITING_PAYMENT, //COD | (Chờ thanh toán) - Dành cho đơn hàng thanh toán khi nhận hàng (COD) hoặc khi khách hàng lựa chọn phương thức thanh toán online nhưng chưa hoàn tất.
//    PAYMENT_REVIEW, // CASH + CREDIT_CARD | (Đang kiểm tra thanh toán) - Áp dụng khi cần xác minh thanh toán từ phía ngân hàng hoặc cổng thanh toán.
//
//    //    Đang xử lý
//    PROCESSING, // (Đang xử lý) - Đơn hàng đã được xác nhận và đang trong quá trình chuẩn bị hàng hóa.
//    PACKAGING, // (Đang đóng gói) - Đơn hàng đã được đóng gói và sẵn sàng giao cho đơn vị vận chuyển.
//
//    //    Đang vận chuyển
//    DELIVERING, // (Đã giao hàng) - Đơn hàng đã được giao cho đơn vị vận chuyển.
//    IN_TRANSIT, // (Đang vận chuyển) - Đơn hàng đang trên đường vận chuyển đến khách hàng.
//    OUT_FOR_DELIVERY, // (Đang giao hàng) - Đơn hàng đã đến khu vực của khách hàng và đang được shipper giao.
//
//    //    Hoàn tất
//    DELIVERED, // (Đã giao hàng thành công) - Khách hàng đã nhận được hàng.
//    COMPLETED, // (Hoàn tất) - Đơn hàng đã hoàn tất tất cả các công đoạn, có thể bao gồm cả việc đã nhận được đánh giá từ khách hàng.
//
//    //    Không thành công
//    CANCELLED, // (Đã hủy) - Đơn hàng bị hủy bởi khách hàng hoặc bởi quản trị website.
//    REFUNDED, // (Đã hoàn tiền) - Khách hàng đã được hoàn tiền cho đơn hàng (nếu đã thanh toán trước).
//    FAILED, // (Thất bại) - Đơn hàng không thể hoàn tất do một số lý do như: không liên lạc được với khách hàng, lỗi giao hàng,...
}
