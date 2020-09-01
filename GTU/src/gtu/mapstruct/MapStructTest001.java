package gtu.mapstruct;

public class MapStructTest001 {

    public static void main(String[] args) {
        // given
        Car car = new Car("Morris", 5, CarType.SEDAN);

        // when
        CarDto carDto = CarMapper.INSTANCE.carToCarDto(car);

        // then
        System.out.println(carDto.getMake());
        System.out.println(carDto.getSeatCount());
        System.out.println(carDto.getType());
    }
}
