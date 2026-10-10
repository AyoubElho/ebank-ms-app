import { ComponentFixture, TestBed } from '@angular/core/testing';
import { BootUi } from './boot-ui';

describe('BootUi', () => {
  let component: BootUi;
  let fixture: ComponentFixture<BootUi>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [BootUi],
    }).compileComponents();

    fixture = TestBed.createComponent(BootUi);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
